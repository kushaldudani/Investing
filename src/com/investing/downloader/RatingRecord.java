package com.investing.downloader;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RatingRecord")
public class RatingRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1679478576333036370L;

	@Id
	@Column(name = "Symbol")
    private String symbol;
    
	@Column(name = "Currentprice")
    private double currentprice;
    
    @Column(name = "Targetprice")
    private double targetprice;
	
    @Column(name = "Sbuy1")
    private int sbuy1;
    
    @Column(name = "Buy1")
    private int buy1;
    
    @Column(name = "Hold1")
    private int hold1;
    
    @Column(name = "Sell1")
    private int sell1;
    
    @Column(name = "Ssell1")
    private int ssell1;
    
    @Column(name = "Mean1")
    private double mean1;
   	
	


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
	 * @return the currentprice
	 */
	public double getCurrentprice() {
		return currentprice;
	}




	/**
	 * @param currentprice the currentprice to set
	 */
	public void setCurrentprice(double currentprice) {
		this.currentprice = currentprice;
	}




	/**
	 * @return the targetprice
	 */
	public double getTargetprice() {
		return targetprice;
	}




	/**
	 * @param targetprice the targetprice to set
	 */
	public void setTargetprice(double targetprice) {
		this.targetprice = targetprice;
	}




	/**
	 * @return the sbuy1
	 */
	public int getSbuy1() {
		return sbuy1;
	}




	/**
	 * @param sbuy1 the sbuy1 to set
	 */
	public void setSbuy1(int sbuy1) {
		this.sbuy1 = sbuy1;
	}




	/**
	 * @return the buy1
	 */
	public int getBuy1() {
		return buy1;
	}




	/**
	 * @param buy1 the buy1 to set
	 */
	public void setBuy1(int buy1) {
		this.buy1 = buy1;
	}




	/**
	 * @return the hold1
	 */
	public int getHold1() {
		return hold1;
	}




	/**
	 * @param hold1 the hold1 to set
	 */
	public void setHold1(int hold1) {
		this.hold1 = hold1;
	}




	/**
	 * @return the sell1
	 */
	public int getSell1() {
		return sell1;
	}




	/**
	 * @param sell1 the sell1 to set
	 */
	public void setSell1(int sell1) {
		this.sell1 = sell1;
	}




	/**
	 * @return the ssell1
	 */
	public int getSsell1() {
		return ssell1;
	}




	/**
	 * @param ssell1 the ssell1 to set
	 */
	public void setSsell1(int ssell1) {
		this.ssell1 = ssell1;
	}




	/**
	 * @return the mean1
	 */
	public double getMean1() {
		return mean1;
	}




	/**
	 * @param mean1 the mean1 to set
	 */
	public void setMean1(double mean1) {
		this.mean1 = mean1;
	}




	public RatingRecord() {
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RatingRecord [symbol=" + symbol + ", currentprice="
				+ currentprice + ", targetprice=" + targetprice + ", sbuy1="
				+ sbuy1 + ", buy1=" + buy1 + ", hold1=" + hold1 + ", sell1="
				+ sell1 + ", ssell1=" + ssell1 + ", mean1=" + mean1 + "]";
	}


	
	
}
