package com.investing.startday;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Stats")
public class Stats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6637981731439067266L;

	@Id
	@Column(name = "Symbol")
    private String symbol;
    
    @Column(name = "Close")
    private double close;
    
    @Column(name = "High")
    private double high;
    
    @Column(name = "Low")
    private double low;

    @Column(name = "Rsi")
    private double rsi;
    
    @Column(name = "Sma200")
    private double sma200;
    
    @Column(name = "Sma5")
    private double sma5;
    
    @Column(name = "Macd")
    private double macd;

    @Column(name = "Prevclose")
    private double prevclose;
    
    @Column(name = "Secondprevclose")
    private double secondprevclose;
    
    @Column(name = "Ema5")
    private double ema5;
    
    @Column(name = "Ema20")
    private double ema20;
    
    @Column(name = "Ema50")
    private double ema50;
    
    @Column(name = "Ema200")
    private double ema200;
    
    @Column(name = "Ma15")
    private double ma15;
    
    @Column(name = "Ema3")
    private double ema3;
    
    @Column(name = "Ema13")
    private double ema13;
    
    @Column(name = "AvgVol")
    private double avgvol;
    
    @Column(name = "Nextmaxclose")
    private double nextmaxclose;
    
    @Column(name = "Nextminclose")
    private double nextminclose;
	
	
	public Stats() {
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 * @return the high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(double low) {
		this.low = low;
	}

	
	public double getRsi() {
		return rsi;
	}

	
	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	/**
	 * @return the sma200
	 */
	public double getSma200() {
		return sma200;
	}

	/**
	 * @param sma200 the sma200 to set
	 */
	public void setSma200(double sma200) {
		this.sma200 = sma200;
	}

	/**
	 * @return the sma5
	 */
	public double getSma5() {
		return sma5;
	}

	/**
	 * @param sma5 the sma5 to set
	 */
	public void setSma5(double sma5) {
		this.sma5 = sma5;
	}

	
	public double getMacd() {
		return macd;
	}

	
	public void setMacd(double macd) {
		this.macd = macd;
	}

	
	/**
	 * @return the prevclose
	 */
	public double getPrevclose() {
		return prevclose;
	}

	/**
	 * @param prevclose the prevclose to set
	 */
	public void setPrevclose(double prevclose) {
		this.prevclose = prevclose;
	}

	/**
	 * @return the secondprevclose
	 */
	public double getSecondprevclose() {
		return secondprevclose;
	}

	/**
	 * @param secondprevclose the secondprevclose to set
	 */
	public void setSecondprevclose(double secondprevclose) {
		this.secondprevclose = secondprevclose;
	}

	/**
	 * @return the ema5
	 */
	public double getEma5() {
		return ema5;
	}

	/**
	 * @param ema5 the ema5 to set
	 */
	public void setEma5(double ema5) {
		this.ema5 = ema5;
	}

	/**
	 * @return the ema20
	 */
	public double getEma20() {
		return ema20;
	}

	/**
	 * @param ema20 the ema20 to set
	 */
	public void setEma20(double ema20) {
		this.ema20 = ema20;
	}

	/**
	 * @return the ema50
	 */
	public double getEma50() {
		return ema50;
	}

	/**
	 * @param ema50 the ema50 to set
	 */
	public void setEma50(double ema50) {
		this.ema50 = ema50;
	}

	/**
	 * @return the ema200
	 */
	public double getEma200() {
		return ema200;
	}

	/**
	 * @param ema200 the ema200 to set
	 */
	public void setEma200(double ema200) {
		this.ema200 = ema200;
	}

	/**
	 * @return the ma15
	 */
	public double getMa15() {
		return ma15;
	}

	/**
	 * @param ma15 the ma15 to set
	 */
	public void setMa15(double ma15) {
		this.ma15 = ma15;
	}

	/**
	 * @return the nextmaxclose
	 */
	public double getNextmaxclose() {
		return nextmaxclose;
	}

	/**
	 * @param nextmaxclose the nextmaxclose to set
	 */
	public void setNextmaxclose(double nextmaxclose) {
		this.nextmaxclose = nextmaxclose;
	}

	/**
	 * @return the nextminclose
	 */
	public double getNextminclose() {
		return nextminclose;
	}

	/**
	 * @param nextminclose the nextminclose to set
	 */
	public void setNextminclose(double nextminclose) {
		this.nextminclose = nextminclose;
	}

	/**
	 * @return the ema3
	 */
	public double getEma3() {
		return ema3;
	}

	/**
	 * @param ema3 the ema3 to set
	 */
	public void setEma3(double ema3) {
		this.ema3 = ema3;
	}

	/**
	 * @return the ema13
	 */
	public double getEma13() {
		return ema13;
	}

	/**
	 * @param ema13 the ema13 to set
	 */
	public void setEma13(double ema13) {
		this.ema13 = ema13;
	}

	/**
	 * @return the avgvol
	 */
	public double getAvgvol() {
		return avgvol;
	}

	/**
	 * @param avgvol the avgvol to set
	 */
	public void setAvgvol(double avgvol) {
		this.avgvol = avgvol;
	}

	@Override
	public String toString() {
		return symbol + "\t" + close + "\t"
				+ high + "\t" + low + "\t" + rsi
				+ "\t" + sma200 + "\t" + sma5 
				+ "\t" + macd
				+ "\t" + prevclose + "\t" + secondprevclose
				+ "\t" + ema5 + "\t" + ema20
				+ "\t" + ema50 + "\t" + ema200
				+ "\t" + ma15 + "\t" + ema3 + "\t" + ema13
				+ "\t" + avgvol + "\t" + nextmaxclose + "\t" + nextminclose;
	}
    
	
}
