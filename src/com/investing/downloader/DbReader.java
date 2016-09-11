package com.investing.downloader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class DbReader {
	
	private Session session;
	
	public void openSession(){
		try{
			Logger log = Logger.getLogger("org.hibernate");
			log.setLevel(Level.WARNING);
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
			session = sessionFactory.openSession();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbReader openSession", 
            		e);
		}
	}
	
	
	
	public void insertOrUpdate(RatingRecord ratingrecord){
		try{
			session.beginTransaction();  
        
			
			session.saveOrUpdate(ratingrecord);
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbReader insertOrUpdate"+ratingrecord.getSymbol(), 
            		e);
		}
	}
	
	public void closeSession(){
		try{
			session.close();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbReader closeSession", 
            		e);
		}
	}

}
