package com.investing.startday;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class RunBacktest {
	
	public static void main(String[] args) {
		futuremain();
	}
	
	public static void futuremain() {
		String whichTable = "Stats";
		
		
		StatsManager frm = new StatsManager();
		frm.openSession();
		List<String> dates = readDates();
		for(int i=3;i<dates.size();i++){
			String date = dates.get(i);
			
			String[] values = date.split("-");
			int year = Integer.parseInt(values[0]);
			int month = Integer.parseInt(values[1]);
			//int day = Integer.parseInt(values[2]);
			if(year == 2015 && (month == 6 || month == 5 || month == 4)){
				
				

				FindStocks.mainrun(date);
			
				List<String> companys = frm.downloadData(whichTable);
				
				for(String company : companys){
					String[] fields = company.split("\t");
					//String symbol = fields[0].split("-")[0];
					
					writeBactest(fields[0], fields[1], fields[2], fields[3],fields[4],fields[5],fields[6],
							fields[7],fields[8],fields[9],fields[10],fields[11],fields[12],fields[13],fields[14],fields[15],fields[16], fields[17], fields[18], fields[19]);
				}
			}
		}
		frm.closeSession();
		//
	}
	
	
	
	public static List<String> readDates(){
		InputStreamReader is = null;
		BufferedReader br = null;
		List<String> dates = new ArrayList<String>();
		
		try {
			is = new InputStreamReader(new FileInputStream(new 
					File("data/dates.txt")));
			br =  new BufferedReader(is);
			
			
			String line; 
			while ((line = br.readLine()) != null) {
				String date = line.trim();
				dates.add(date);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {
				br.close();
				is.close();
				
			} catch (IOException e) {}
		}
		return dates;
	}
	
	public static void writeBactest(String symbol, String close, String high,
			String low, String rsi, String sma200, String sma5, String macd, String prevclose,
			String secondprevclose, String ema5, String ema20, String ema50, String ema200, String ma15,
			String ema3, String ema13, String avgvol, String next30max, String next30min) {
		OutputStreamWriter out = null;
		BufferedWriter bw = null;
		try{
			out = new OutputStreamWriter(new FileOutputStream(new 
					File("config/result.csv"),true));
			bw =  new BufferedWriter(out);
		
			String output = symbol + ";" +
					close + ";" + high + ";" +
					low + ";" + rsi + ";" + sma200 + ";" + sma5  
					+ ";" + macd + ";" + prevclose + ";" + secondprevclose
					+ ";" + ema5 + ";" + ema20 + ";" + ema50
					+ ";" + ema200 + ";" + ma15 + ";" + ema3 + ";" + ema13 + ";" + avgvol + ";" + next30max
					+ ";" + next30min ;
			
			bw.write(output);
			bw.write("\n");
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {
				 bw.close();
				out.close();
			} catch (IOException e) {}
		}
		
	}

}
