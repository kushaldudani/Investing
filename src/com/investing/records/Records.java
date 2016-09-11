package com.investing.records;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Records")
public class Records implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4494800808974465062L;

	@Id
	@Column(name = "Symbol")
    private String symbol;
    
	@Column(name = "Open")
    private double open;
    
    @Column(name = "High")
    private double high;
	
    @Column(name = "Low")
    private double low;
    
    @Column(name = "Close")
    private double close;
    
    @Column(name = "Volume")
    private double volume;
   	
	
    public Records() {
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
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}


	/**
	 * @param open the open to set
	 */
	public void setOpen(double open) {
		this.open = open;
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
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}


	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}


	
	
}
