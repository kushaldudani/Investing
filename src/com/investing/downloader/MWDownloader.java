package com.investing.downloader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;



public class MWDownloader {
	
	public static void main(String[] args) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			MWDownloader downloader = new MWDownloader();
			List<RatingRecord> records = new ArrayList<RatingRecord>();
			List<String> scrips = readScrips();
			SynQueue<String> queue = new SynQueue<String>();
			for(String scrip : scrips){
				queue.enqueue(scrip);
			}
			String stock = null;
			String date = sdf.format(new Date());
			while((stock = queue.dequeue()) != null){
				RatingRecord rr = downloader.downloadData(stock, date);
				if(rr == null){
					queue.enqueue(stock);
				}else{
					records.add(rr);
				}
			}
			
			DbReader db = new DbReader();
			db.openSession();
			for(RatingRecord entry : records){
				LoggerUtil.getLogger().info(entry.toString());
				if(entry.getMean1() < 100 && entry.getMean1() > -100 && 
						entry.getCurrentprice() != Double.NaN && entry.getTargetprice() != Double.NaN){
					db.insertOrUpdate(entry);
				}else{
					LoggerUtil.getLogger().info("Failed to fetch valid data for " + entry.getSymbol());
				}
			}
			db.closeSession();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "MWDownloader exception in main method", 
            		e);
		}
	}
	
	private static List<String> readScrips(){
		InputStreamReader is = null;
		BufferedReader br = null;
		List<String> scrips = new ArrayList<String>();
		
		try {
			is = new InputStreamReader(new FileInputStream(new 
					File("data/nyse100.txt")));
			br =  new BufferedReader(is);
			String line; 
			while ((line = br.readLine()) != null) {
				String scrip = line.trim();
				scrips.add(scrip);
			}
		} catch (Exception e) {
			LoggerUtil.getLogger().log(Level.SEVERE, "readScrips failed", 
            		e);
			System.exit(1);
		}finally{
			 try {
				br.close();
				is.close();
				
			} catch (IOException e) {}
		}
		return scrips;
	}
	
	
	private static final String baseUrl = "http://www.marketwatch.com/investing/Stock/";
	private static final String urlsuffix = "/analystestimates?CountryCode=US";
	private HttpClient client;

	
	public MWDownloader() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*1000).setConnectTimeout(60*1000).build();
		client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}
	
	
	
	
	private RatingRecord downloadData(String symbol, String date){
		RatingRecord rr = new RatingRecord();
		rr.setSymbol(symbol+"-"+date);
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try{
			HttpResponse response = retry(symbol);
			if(response.getStatusLine().getStatusCode() == 200){
				inputStreamReader = new InputStreamReader(response.getEntity().getContent());
				bufferedReader = new BufferedReader(inputStreamReader);
				String line;  
				while ((line = bufferedReader.readLine()) != null) {
					if(line.contains("lastcolumn data bgLast price")){
						bufferedReader.readLine();
						line = bufferedReader.readLine();
						String price = line.replaceAll("\\s", "");
						price = price.replace(",", "");
						rr.setCurrentprice(Double.parseDouble(price));
					}else if(line.contains("Average Target Price")){
						line = bufferedReader.readLine();
						String targetprice = (line.split(">")[1]).split("<")[0];
						targetprice = targetprice.replace(",", "");
						rr.setTargetprice(Double.parseDouble(targetprice));
					}else if(line.contains("\"first\">BUY")){
						line = bufferedReader.readLine();
						String sbuy1 = (line.split(">")[1]).split("<")[0];
						rr.setSbuy1(Integer.parseInt(sbuy1));
					}else if(line.contains("\"first\">OVERWEIGHT")){
						line = bufferedReader.readLine();
						String buy1 = (line.split(">")[1]).split("<")[0];
						rr.setBuy1(Integer.parseInt(buy1));
					}else if(line.contains("\"first\">HOLD")){
						line = bufferedReader.readLine();
						String hold1 = (line.split(">")[1]).split("<")[0];
						rr.setHold1(Integer.parseInt(hold1));
					}else if(line.contains("\"first\">UNDERWEIGHT")){
						line = bufferedReader.readLine();
						String sell1 = (line.split(">")[1]).split("<")[0];
						rr.setSell1(Integer.parseInt(sell1));
					}else if(line.contains("\"first\">SELL")){
						line = bufferedReader.readLine();
						String ssell1 = (line.split(">")[1]).split("<")[0];
						rr.setSsell1(Integer.parseInt(ssell1));
					}
				}
			}else{
				return null;
			}
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "MWDownloader downloadData", 
            		e);
			return null;
		}finally{
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {}
			}
			if(inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {}
			}
		}
		
		int total = rr.getSbuy1()+rr.getBuy1()+rr.getHold1()+rr.getSell1()+rr.getSsell1();
		int weight = (rr.getSbuy1()+(2*rr.getBuy1())+(3*rr.getHold1())+(4*rr.getSell1())+(5*rr.getSsell1()));
		double mean1 = (double)((double)weight/(double)total);
		rr.setMean1(mean1);
		return rr;
	}
	
	

	private HttpResponse retry(String symbol) {
		String finalurl = getUrl(symbol);
		LoggerUtil.getLogger().info(finalurl);
		HttpGet request = new HttpGet(finalurl);
		request.setHeader("User-Agent", "runscope/0.1");
		request.setHeader("Accept-Encoding", "gzip, deflate");
		request.setHeader("Accept", "*/*");
		int responsecode=0;
		int nooftries = 1;
		HttpResponse response=null;
		while(responsecode != 200 && nooftries <= 5){
			try{
				response = client.execute(request);
				responsecode = response.getStatusLine().getStatusCode();
			}catch(Exception e){e.printStackTrace();}
			try {
				Thread.sleep(nooftries * 1000);
			} catch (InterruptedException e) {}
			nooftries++;
		}
		
		return response;
	}
	
	
	
	
	private String getUrl(String symbol) {
		String finalurl = baseUrl + symbol + urlsuffix;
		return finalurl;
	}

}
