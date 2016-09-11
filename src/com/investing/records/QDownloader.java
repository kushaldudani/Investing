package com.investing.records;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.investing.downloader.LoggerUtil;



public class QDownloader {
	
	public static void main(String[] args) {
		try{
			String startdate = "2011-01-01";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String enddate = sdf.format(new Date());
			QDownloader downloader = new QDownloader();
			List<String> scrips = readScrips();
			DbManager db = new DbManager();
			for (String scrip : scrips) {
				db.openSession();
				String lastdate = db.getLastRecordDate(scrip);
				if (lastdate == null || enddate.compareTo(lastdate) > 0) {
					if(lastdate == null){
						lastdate = startdate;
					}else {
						Calendar lastDate = Calendar.getInstance();
						lastDate.setTime(sdf.parse(lastdate));
						lastDate.add(Calendar.DAY_OF_MONTH, 1);
						lastdate = sdf.format(lastDate.getTime());
					}
					List<Records> records = downloader.downloadData(scrip, lastdate, enddate);
					for (Records r : records) {
						db.insertOrUpdate(r);
					}
				}
				db.closeSession();
			}
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "QDownloader exception in main method", 
            		e);
		}
	}
	
	private static List<String> readScrips(){
		InputStreamReader is = null;
		BufferedReader br = null;
		List<String> scrips = new ArrayList<String>();
		
		try {
			is = new InputStreamReader(new FileInputStream(new 
					File("data/qscrips.txt")));
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
	
	
	private static final String baseUrl = "https://www.quandl.com/api/v3/datasets/GOOG/";
	private HttpClient client;

	
	public QDownloader() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60*1000).setConnectTimeout(60*1000).build();
		client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}
	
	
	
	
	private List<Records> downloadData(String symbol, String startdate, String enddate){
		List<Records> records = new ArrayList<Records>();
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try{
			HttpResponse response = retry(symbol, startdate, enddate);
			if(response.getStatusLine().getStatusCode() == 200){
				inputStreamReader = new InputStreamReader(response.getEntity().getContent());
				bufferedReader = new BufferedReader(inputStreamReader);
				String line;  
				while ((line = bufferedReader.readLine()) != null) {
					if(line.charAt(0) != '2'){
						continue;
					}
					String[] vals = line.split(",");
					Records r = new Records();
					r.setSymbol(symbol+"-"+vals[0]);
					if(vals[1].length() != 0){r.setOpen(Double.parseDouble(vals[1]));}
					if(vals[2].length() != 0){r.setHigh(Double.parseDouble(vals[2]));}
					if(vals[3].length() != 0){r.setLow(Double.parseDouble(vals[3]));}
					r.setClose(Double.parseDouble(vals[4]));
					if(vals.length == 6){r.setVolume(Double.parseDouble(vals[5]));}
					records.add(r);
				}
			}else{
				return null;
			}
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "QDownloader downloadData", e);
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
		
		return records;
	}
	
	

	private HttpResponse retry(String symbol, String startdate, String enddate) {
		String finalurl = getUrl(symbol, startdate, enddate);
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
	
	
	
	
	private String getUrl(String symbol, String startdate, String enddate) {
		String finalurl = baseUrl + symbol + ".csv?auth_token=ZoVSx7zMTiAVUptENc_K&sort_order=asc&collapse=daily&start_date="+startdate+"&end_date="+enddate;
		return finalurl;
	}

}
