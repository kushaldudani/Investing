package com.investing.records;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.investing.downloader.HibernateUtil;
import com.investing.downloader.LoggerUtil;


public class DbManager {
	
	private Session session;
	
	public void openSession(){
		try{
			Logger log = Logger.getLogger("org.hibernate");
			log.setLevel(Level.WARNING);
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
			session = sessionFactory.openSession();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager openSession", 
            		e);
		}
	}
	
	
	
	public void insertOrUpdate(Records record){
		try{
			session.beginTransaction();  
        
			
			session.saveOrUpdate(record);
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager insertOrUpdate"+record.getSymbol(), 
            		e);
		}
	}
	
	public String getLastRecordDate(String stock) {
		Records record=null;
		String lastdate = null;
		try{
		session.beginTransaction();
		
		String hql = "Select * from Records where Symbol like '"+stock+"-2%' order by Symbol desc";
	    SQLQuery query = session.createSQLQuery(hql);
	    query.addEntity(Records.class);
	    List<Object> records = query.list();
	    Object o = records.get(0);
	    record = (Records) o;
	    String symbol = record.getSymbol();
	    String[] values = symbol.split("-");
	    lastdate = values[1]+"-"+values[2]+"-"+values[3];
	    
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager getLastRecordDate failed", e);
		}
		return lastdate;
	}
	
	public List<Records> getRecords(String stock) {
		List<Records> records = new ArrayList<Records>();
		try{
		session.beginTransaction();
		
		String hql = "Select * from Records where Symbol like '"+stock+"-2%' order by Symbol asc";
	    SQLQuery query = session.createSQLQuery(hql);
	    query.addEntity(Records.class);
	    List<Object> objects = query.list();
	    for(Object o : objects){
	    	Records fr = (Records) o;
	    	records.add(fr);
	    }
	    
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager getRecords failed", e);
		}
		return records;
	}
	
	/*public List<Records> getRecords(String stock, String lastrecord) {
		List<Records> records = new ArrayList<Records>();
		try{
		session.beginTransaction();
		
		String hql = "Select * from Records where Symbol like '"+stock+"-2%' order by Symbol asc";
	    SQLQuery query = session.createSQLQuery(hql);
	    query.addEntity(Records.class);
	    List<Object> objects = query.list();
	    for(Object o : objects){
	    	Records fr = (Records) o;
	    	String[] values = fr.getSymbol().split("-");
	    	String date = values[1]+"-"+values[2]+"-"+values[3];
	    	if(date.compareTo(lastrecord) <= 0){
	    		records.add(fr);
	    	}
	    }
	    
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager getRecords failed", e);
		}
		return records;
	}
	
	public List<Records> getRecords(String stock, String firstrecord, String lastrecord) {
		List<Records> records = new ArrayList<Records>();
		try{
		session.beginTransaction();
		
		String hql = "Select * from Records where Symbol like '"+stock+"-2%' order by Symbol asc";
	    SQLQuery query = session.createSQLQuery(hql);
	    query.addEntity(Records.class);
	    List<Object> objects = query.list();
	    for(Object o : objects){
	    	Records fr = (Records) o;
	    	String[] values = fr.getSymbol().split("-");
	    	String date = values[1]+"-"+values[2]+"-"+values[3];
	    	if(date.compareTo(firstrecord) > 0 && date.compareTo(lastrecord) <= 0){
	    		records.add(fr);
	    	}
	    }
	    
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager getRecords failed", e);
		}
		return records;
	}*/
	
	public void closeSession(){
		try{
			session.close();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager closeSession", 
            		e);
		}
	}

}
