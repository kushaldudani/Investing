package com.investing.records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.investing.downloader.LoggerUtil;


public class Calculation {
	
	
	
	public static void main(String[] args) {
		String startdate = "2013-02-01";
		int monthsdiff = 3;
		List<String> scrips = readScrips();
		for (String scrip : scrips) {
			Map<String, Double> result = calculateMovements(scrip, startdate, monthsdiff);
			System.out.print(scrip+";");
			for (String key : result.keySet()){
				System.out.print(key+"/"+result.get(key));
				System.out.print(";");
			}
			System.out.println("");
		}
	}
	
	private static Map<String, Double> calculateMovements(String scrip, String startdate, int monthsdiff) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Double> result = new LinkedHashMap<String, Double>();
		DbManager db = new DbManager();
		db.openSession();
		List<Records> records = db.getRecords(scrip);
		db.closeSession();
		try {
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(sdf.parse(startdate));
			double enterclose = -1;
			String enterdate = "";
			int sz = records.size();
			for (int i = 0; i < sz; i++) {
				Records record = records.get(i);
				String st = record.getSymbol();
				String[] values = st.split("-");
				String dt = values[1] + "-" + values[2] + "-" + values[3];
				String currentdate = sdf.format(currentDate.getTime());
				if (dt.compareTo(currentdate) >= 0 || i == (sz-1)) {
					double exitclose = record.getClose();
					if(enterclose != -1){
						double move = (((exitclose-enterclose)/enterclose)*100);
						result.put(enterdate+"/"+currentdate, move);
						enterclose = exitclose;
						enterdate = currentdate;
					}else {
						enterclose = record.getClose();
						enterdate = currentdate;
					}
					currentDate.add(Calendar.MONTH, monthsdiff); 
				}
			}
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "Calculation exception in calculateMovements", e);
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
}
