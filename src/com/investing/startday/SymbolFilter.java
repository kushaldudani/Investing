package com.investing.startday;
import java.util.ArrayList;
import java.util.List;

import com.investing.downloader.LoggerUtil;
import com.investing.records.Records;



public class SymbolFilter {
	
	
	private List<Double> macd;
	private List<Double> ema12;
	private List<Double> ema26;
	private List<Double> signal;
	private List<Double> hist;
	
	private List<Double> gain;
	private List<Double> loss;
	private List<Double> avggain;
	private List<Double> avgloss;
	private List<Double> rs;
	private List<Double> rsi;
	
	private List<Double> sma200;
	private List<Double> sma5;
	
	private List<Double> ema5;
	private List<Double> ema20;
	private List<Double> ema50;
	private List<Double> ema200;
	private List<Double> ma15;
	private List<Double> ema3;
	private List<Double> ema13;
	
	
 	private String symbol;
	
	
	public SymbolFilter(String symbol) {
		this.symbol = symbol;
	}
	
	public Stats filterStock(List<Records> records){
		int size = records.size();
		if(size < 200){
			LoggerUtil.getLogger().info(symbol + "-Not Sufficient Records SymbolFilter");
			return null;
		}
		if(records.get(size-1).getClose() <=0 || 
				records.get(size-1).getHigh() <=0 ||
				records.get(size-1).getLow() <=0 ||
				records.get(size-2).getClose() <=0 || 
				records.get(size-2).getHigh() <=0 ||
				records.get(size-2).getLow() <=0){
			LoggerUtil.getLogger().info(symbol + "-Records in SymbolFilter with zero entries");
			return null;
		}
		initializeArrays(size);
		calculateSMA200(records);
		calculateSMA5(records);
		calculateRSI(records, 14);
		calculateMACD(records);
		calculateSMA15(records);
		ema5(records);
		ema20(records);
		ema50(records);
		ema200(records);
		ema3(records);
		ema13(records);
		
		
		Stats stats = new Stats();
		stats.setSymbol(records.get(size-1).getSymbol());
		stats.setClose(records.get(size-1).getClose());
		stats.setHigh(records.get(size-1).getHigh());
		stats.setLow(records.get(size-1).getLow());
		stats.setPrevclose(records.get(size-2).getClose());
		stats.setSecondprevclose(records.get(size-3).getClose());
		
		stats.setRsi(rsi.get(size-1));
		stats.setMacd(macd.get(size-1));
		
		
		
		stats.setSma200((((records.get(size-1).getClose()-sma200.get(size-1))/records.get(size-1).getClose())*100));
		stats.setSma5((((records.get(size-1).getClose()-sma5.get(size-1))/records.get(size-1).getClose())*100));
		
		stats.setEma5(ema5.get(size-1));
		stats.setEma20(ema20.get(size-1));
		stats.setEma50(ema50.get(size-1));
		stats.setEma200(ema200.get(size-1));
		stats.setMa15(ma15.get(size-1));
		stats.setEma3(ema3.get(size-1));
		stats.setEma13(ema13.get(size-1));
		stats.setAvgvol(getAverageVolume(records, size-30, size));
		
		return stats;
	}
	
	
	
	
	private void calculateSMA200(List<Records> records){
		int size = records.size();
		
		for (int i = 199; i < size; i++) {
            double nextval = getAverageClose(records, i-99, i+1);
            sma200.set(i, nextval);
        }
	}
	
	private void calculateSMA5(List<Records> records){
		int size = records.size();
		
		for (int i = 4; i < size; i++) {
            double nextval = getAverageClose(records, i-4, i+1);
            sma5.set(i, nextval);
        }
	}
	
	private void calculateSMA15(List<Records> records){
		int size = records.size();
		
		for (int i = 14; i < size; i++) {
            double nextval = getAverageClose(records, i-14, i+1);
            ma15.set(i, nextval);
        }
	}
	
	private void calculateRSI(List<Records> records, int period){
		int size = records.size();
		
		for (int i = 1; i < size; i++) {
            double change = records.get(i).getClose() - records.get(i-1).getClose();
            gain.set(i,Math.max(0.0, change));
            loss.set(i,Math.max(0.0, -change));
        }
		
		avggain.set(period, getAverage(gain, 1, period+1));
		for(int i=period+1;i<size;i++){
			double nextval = ((avggain.get(i-1)*(period-1))+gain.get(i))/(period);
			avggain.set(i, nextval);
		}
		
		avgloss.set(period, getAverage(loss, 1, period+1));
		for(int i=period+1;i<size;i++){
			double nextval = ((avgloss.get(i-1)*(period-1))+loss.get(i))/(period);
			avgloss.set(i, nextval);
		}
		
		for(int i=period;i<size;i++){
			double nextval=100.0;
			if(avgloss.get(i) != 0){
				nextval = avggain.get(i)/avgloss.get(i);
			}
			rs.set(i, nextval);
		}
		
		for(int i=period;i<size;i++){
			double nextval=(100-(100/(1+rs.get(i))));
			rsi.set(i, nextval);
		}
	}
	
	private void ema5(List<Records> records){
		int size = records.size();
		ema5.set(4,getAverageClose(records, 0, 5));
		for(int i=5;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(5+1))) + (ema5.get(i-1) * (1-((double)(2)/(double)(5+1))));
			ema5.set(i,nextema);
		}
	}
	private void ema20(List<Records> records){
		int size = records.size();
		ema20.set(19,getAverageClose(records, 0, 20));
		for(int i=20;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(20+1))) + (ema20.get(i-1) * (1-((double)(2)/(double)(20+1))));
			ema20.set(i,nextema);
		}
	}
	private void ema50(List<Records> records){
		int size = records.size();
		ema50.set(49,getAverageClose(records, 0, 50));
		for(int i=50;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(50+1))) + (ema50.get(i-1) * (1-((double)(2)/(double)(50+1))));
			ema50.set(i,nextema);
		}
	}
	private void ema200(List<Records> records){
		int size = records.size();
		ema200.set(199,getAverageClose(records, 0, 200));
		for(int i=200;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(200+1))) + (ema200.get(i-1) * (1-((double)(2)/(double)(200+1))));
			ema200.set(i,nextema);
		}
	}
	
	private void ema3(List<Records> records){
		int size = records.size();
		ema3.set(2,getAverageClose(records, 0, 3));
		for(int i=3;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(3+1))) + (ema3.get(i-1) * (1-((double)(2)/(double)(3+1))));
			ema3.set(i,nextema);
		}
	}
	
	private void ema13(List<Records> records){
		int size = records.size();
		ema13.set(12,getAverageClose(records, 0, 13));
		for(int i=13;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(13+1))) + (ema13.get(i-1) * (1-((double)(2)/(double)(13+1))));
			ema13.set(i,nextema);
		}
	}
	
	
	private void calculateMACD(List<Records> records){
		int size = records.size();
		
		ema12.set(11,getAverageClose(records, 0, 12));
		for(int i=12;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(12+1))) + (ema12.get(i-1) * (1-((double)(2)/(double)(12+1))));
			ema12.set(i,nextema);
		}
		
		ema26.set(25,getAverageClose(records, 0, 26));
		for(int i=26;i<size;i++){
			double nextema = ((records.get(i).getClose())*((double)(2)/(double)(26+1))) + (ema26.get(i-1) * (1-((double)(2)/(double)(26+1))));
			ema26.set(i,nextema);
		}
		
		for(int i=25;i<size;i++){
			macd.set(i, (ema12.get(i)-ema26.get(i)));
		}
		
		signal.set(33, getAverage(macd, 25, 34));
		for(int i=34;i<size;i++){
			double nextsignal = ((macd.get(i))*((double)(2)/(double)(9+1))) + (signal.get(i-1) * (1-((double)(2)/(double)(9+1))));
			signal.set(i, nextsignal);
		}
		
		for(int i=33;i<size;i++){
			hist.set(i, (macd.get(i)-signal.get(i)));
		}
	}
	
	private double getAverageClose(List<Records> records, int start, int end){
		double avg = 0.0;
		for(int i=start;i<end;i++){
			avg = avg + records.get(i).getClose();
		}
		avg = avg/((double)(end-start));
		return avg;
	}
	
	private double getAverageVolume(List<Records> records, int start, int end){
		double avg = 0.0;
		for(int i=start;i<end;i++){
			avg = avg + records.get(i).getVolume();
		}
		avg = avg/((double)(end-start));
		return avg;
	}
	
	
	
	private double getAverage(List<Double> values, int start, int end){
		double avg = 0.0;
		for(int i=start;i<end;i++){
			avg = avg + values.get(i);
		}
		avg = avg/((double)(end-start));
		return avg;
	}
	
	

	
	private void initializeArrays(int size){
		macd = new ArrayList<Double>(size);
		ema12 = new ArrayList<Double>(size);
		ema26 = new ArrayList<Double>(size);
		signal = new ArrayList<Double>(size);
		hist = new ArrayList<Double>(size);
		gain = new ArrayList<Double>(size);
		loss = new ArrayList<Double>(size);
		avggain = new ArrayList<Double>(size);
		avgloss = new ArrayList<Double>(size);
		rs = new ArrayList<Double>(size);
		rsi = new ArrayList<Double>(size);
		sma200 = new ArrayList<Double>(size);
		sma5 = new ArrayList<Double>(size);
		ema5 = new ArrayList<Double>(size);
		ema20 = new ArrayList<Double>(size);
		ema50 = new ArrayList<Double>(size);
		ema200 = new ArrayList<Double>(size);
		ma15 = new ArrayList<Double>(size);
		ema3 = new ArrayList<Double>(size);
		ema13 = new ArrayList<Double>(size);
		for(int i=0;i<size;i++){
			macd.add(i, 0.0);
			ema12.add(i, 0.0);
			ema26.add(i, 0.0);
			signal.add(i, 0.0);
			hist.add(i, 0.0);
			gain.add(i, 0.0);
			loss.add(i, 0.0);
			avggain.add(i, 0.0);
			avgloss.add(i, 0.0);
			rs.add(i, 0.0);
			rsi.add(i, 0.0);
			sma200.add(i, 0.0);
			sma5.add(i, 0.0);
			ema5.add(i, 0.0);
			ema20.add(i, 0.0);
			ema50.add(i, 0.0);
			ema200.add(i, 0.0);
			ma15.add(i, 0.0);
			ema3.add(i, 0.0);
			ema13.add(i, 0.0);
		}
	}



	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

}
