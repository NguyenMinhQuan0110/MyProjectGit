package com.haha.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
private static SessionFactory sf;
	
	private HibernateUtils() {
		
	}
	public static SessionFactory getSessionFactory() {
		if (sf==null) {
			//Mơ kết nối với database 
			sf= new Configuration().configure().buildSessionFactory();
			
			
		}
		
		return sf;
	}
}
