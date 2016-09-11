package com.investing.startday;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.investing.downloader.LoggerUtil;
import com.investing.downloader.SynQueue;




public class FindStocks {
	
	
	
	public static void mainrun(String lastRecord){
		SynQueue<String> stocks = new SynQueue<String>();
		List<String> separatelist = new ArrayList<String>();
		StatsManager db = new StatsManager();
		db.openSession();
		db.truncateTable();
		db.closeSession();
		
		InputStreamReader is = null;
		BufferedReader br = null;
		String filename="data/qscrips.txt";
		try {
			is = new InputStreamReader(new FileInputStream(new 
					File(filename)));
			br =  new BufferedReader(is);
			String line;
			while ((line = br.readLine()) != null) {
				String symbol = line.trim();
				stocks.enqueue(symbol);
				separatelist.add(symbol);
			}
		} catch (Exception e) {
			LoggerUtil.getLogger().log(Level.SEVERE, "Stocks symbol read failed", e);
			System.exit(1);
		}finally{
			 try {
				br.close();
				is.close();
			} catch (IOException e) {}
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for(int i=0;i<15;i++){
			executorService.execute(new Worker(stocks,lastRecord));
		}
		executorService.shutdown();
		try {
			boolean result = executorService.awaitTermination(5, TimeUnit.MINUTES);
			LoggerUtil.getLogger().info("Executor service result - " + result);
		} catch (InterruptedException e) {
			LoggerUtil.getLogger().log(Level.SEVERE, "FindStocks Executor failed", e);
			System.exit(1);
		}
	}

}
