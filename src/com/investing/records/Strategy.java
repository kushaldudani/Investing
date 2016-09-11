package com.investing.records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.investing.downloader.LoggerUtil;

public class Strategy {
	
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dates = readDates();
		String startDate = "2015-01-01";
		String endDate = "2015-09-30";
		for(int i=0;i<dates.size();i++){
			String dt = dates.get(i);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(dt));
			cal.add(Calendar.MONTH, 1);
			String dateToMeasure = sdf.format(cal.getTime());
			if(dt.compareTo(startDate) >= 0 && dt.compareTo(endDate) < 0) {
				List<String> scrips = readScrips();
				for (String scrip : scrips) {
					double result = pickingStrategyLong(scrip, dt);
					if(result > 25){
						double measure = measureStrategyLong(scrip, dt, dateToMeasure);
						System.out.println(dt + ";" + scrip + ";"+result + ";" +measure);
					}
				}
			}
		}
	}
	
	private static double pickingStrategyLong(String scrip, String dateToEvaluate) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		double result = -1;
		DbManager db = new DbManager();
		db.openSession();
		List<Records> records = db.getRecords(scrip);
		db.closeSession();
		try {
			int sz = records.size();
			int i = sz-1;
			double maxValue = 0;
			double closeAtEvaluate = 0;
			int cnt = 0;
			while (i >= 0) {
				Records record = records.get(i);
				String st = record.getSymbol();
				String[] values = st.split("-");
				String dt = values[1] + "-" + values[2] + "-" + values[3];
				if (dt.compareTo(dateToEvaluate) <= 0 && cnt < 60) {
					double close = record.getClose();
					if(cnt == 0){ //first entrance in the loop
						closeAtEvaluate = close;
					}
					if(close > maxValue) {
						maxValue = close;
					}
					cnt++;
				}
				i--;
			}
			if(maxValue == 0 || closeAtEvaluate == 0){
				throw new Exception("Something wrong in pickingStrategy");
			}
			result = (((maxValue-closeAtEvaluate)/maxValue)*100);
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "Calculation exception", e);
		}
		
		return result;
	}
	
	private static double measureStrategyLong(String scrip, String dateToEvaluate, String dateToMeasure) {
		double result = -1;
		DbManager db = new DbManager();
		db.openSession();
		List<Records> records = db.getRecords(scrip);
		db.closeSession();
		try {
			int sz = records.size();
			int i = sz-1;
			double closeAtMeasure = 0;
			double closeAtEvaluate = 0;
			boolean recordedCloseAtEvaluate = false;
			while (i >= 0) {
				Records record = records.get(i);
				String st = record.getSymbol();
				String[] values = st.split("-");
				String dt = values[1] + "-" + values[2] + "-" + values[3];
				if (dt.compareTo(dateToEvaluate) <= 0 && !recordedCloseAtEvaluate) {
					closeAtEvaluate = record.getClose();
					recordedCloseAtEvaluate = true;
				}
				if (dt.compareTo(dateToMeasure) <= 0 && dt.compareTo(dateToEvaluate) > 0) {
					double close = record.getClose();
					if(close > closeAtMeasure) {
						closeAtMeasure = close;
					}
				}
				i--;
			}
			if(closeAtMeasure == 0 || closeAtEvaluate == 0){
				throw new Exception("Something wrong in measureStrategy");
			}
			result = (((closeAtMeasure-closeAtEvaluate)/closeAtEvaluate)*100);
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "Calculation exception", e);
		}
		
		return result;
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
	
	private static List<String> readDates(){
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
		} catch (Exception e) {
			LoggerUtil.getLogger().log(Level.SEVERE, "Read Dates failed", e);
			System.exit(1);
		}finally{
			 try {
				br.close();
				is.close();
				
			} catch (IOException e) {}
		}
		return dates;
	}
}
