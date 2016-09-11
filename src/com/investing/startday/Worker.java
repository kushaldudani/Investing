package com.investing.startday;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.investing.downloader.LoggerUtil;
import com.investing.downloader.SynQueue;
import com.investing.records.DbManager;
import com.investing.records.Records;



public class Worker implements Runnable {
	
	private SynQueue<String> stocks;
	private String lastRecord;
	private String next30Record;
	
	public Worker(SynQueue<String> stocks, String lastRecord) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.stocks = stocks;
		this.lastRecord = lastRecord;
		String[] values = lastRecord.split("-");
		int year = Integer.parseInt(values[0]);
		int month = Integer.parseInt(values[1]);
		int day = Integer.parseInt(values[2]);
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,day);
		this.next30Record = sdf.format(cal.getTime());
	}

	@Override
	public void run() {
		//Map<String,String> symbolmap = StockDataUtil.buildSymbolMap();
		StatsManager db = new StatsManager();
		DbManager statsdb = new DbManager();
		statsdb.openSession();
		//MoneyControl mc = new MoneyControl();
		db.openSession();
		String stock = null;
		while((stock = stocks.dequeue()) != null){
			List<Records> records =  statsdb.getRecords(stock);
			if(records == null ){
				stocks.enqueue(stock);
			}else{
				List<Records> reslist = new ArrayList<>();
				for(Records rr : records){
			    	String[] values = rr.getSymbol().split("-");
			    	String date = values[1]+"-"+values[2]+"-"+values[3];
			    	if(date.compareTo(lastRecord) <= 0){
			    		reslist.add(rr);
			    	}
			    }
				List<Records> next30list = new ArrayList<>();
				for(Records rr : records){
			    	String[] values = rr.getSymbol().split("-");
			    	String date = values[1]+"-"+values[2]+"-"+values[3];
			    	if(date.compareTo(lastRecord) > 0 && date.compareTo(next30Record) <= 0){
			    		next30list.add(rr);
			    	}
			    }
				Stats stats=null;
				try{
					SymbolFilter sf = new SymbolFilter(stock);
					stats = sf.filterStock(reslist);
				}catch(Exception e){LoggerUtil.getLogger().log(Level.SEVERE, "Severe Error in FindStocks Worker", e);}
				if(stats != null){
					stats.setNextmaxclose(getNext30MaxClose(next30list));
					stats.setNextminclose(getNext30MinClose(next30list));
					db.insertOrUpdate(stats);
				}else{
					LoggerUtil.getLogger().info("FindStocks WorkerCould not insert in db -" + stock);
				}
			}
		}
		statsdb.closeSession();
		db.closeSession();
	}
	
	private double getNext30MaxClose(List<Records> records){
		double maxClose = 0;
		for(Records rr : records){
			if(rr.getClose() > maxClose){
				maxClose = rr.getClose();
			}
		}
		return maxClose;
	}
	
	private double getNext30MinClose(List<Records> records){
		double minClose = 9999999;
		for(Records rr : records){
			if(rr.getClose() < minClose){
				minClose = rr.getClose();
			}
		}
		return minClose;
	}

}
