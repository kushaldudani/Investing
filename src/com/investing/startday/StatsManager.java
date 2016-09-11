package com.investing.startday;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.investing.downloader.HibernateUtil;
import com.investing.downloader.LoggerUtil;


public class StatsManager {
	
	private Session session;
	
	public void openSession(){
		try{
			Logger log = Logger.getLogger("org.hibernate");
			log.setLevel(Level.WARNING);
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
			session = sessionFactory.openSession();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager opensession failed", e);
			System.exit(1);
		}
	}
	
	public void insertOrUpdate(Stats stats){
		try{
			session.beginTransaction();  
        
			
			session.saveOrUpdate(stats);
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager insertorupdate failed " + 
																				stats.getSymbol(), e);
			System.exit(1);
		}
	}
	
	public void truncateTable() {
		try{
			session.beginTransaction();  
			String hql = String.format("delete from %s","Stats");
		    Query query = session.createQuery(hql);
		    query.executeUpdate();
			
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager truncatetable failed", e);
			System.exit(1);
		}
	}
	
	public void closeSession(){
		try{
			session.close();
		}catch(Exception e){
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager Closesession failed", e);
			System.exit(1);
		}
	}
	
	public Stats getStats(String stock) {
		Stats stats=null;
		try{
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Stats.class);
		criteria.add(Restrictions.eq("symbol", stock));
		
		stats = (Stats) criteria.uniqueResult();
		session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbManager getStats failed", e);
			System.exit(1);
			return null;
		}
		return stats;
	}

	public List<String> downloadData(String tableName) {
		List<String> companylist = new ArrayList<String>();
		try{
			session.beginTransaction();
			List<Object> olist = session.createQuery("from " + tableName).list();
			
			for(Object o : olist){
				companylist.add(o.toString());
			}
			session.getTransaction().commit();
		}catch(Exception e){
			session.getTransaction().rollback();
			LoggerUtil.getLogger().log(Level.SEVERE, "DbReader downloaddata failed", e);
			System.exit(1);
		}
		
		return companylist;
	}
	
}
