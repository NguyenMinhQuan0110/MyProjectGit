package com.haha.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.haha.entity.Images;

public class ImplImagesDao implements IImagesDao {

	private SessionFactory sf;
	
	public ImplImagesDao() {
		sf=HibernateUtils.getSessionFactory();
	}
	@Override
	public boolean insert(Images image) {
		Session session = sf.openSession();
	    Transaction tx =null;
	    try {
	        tx = session.beginTransaction();
	        session.save(image);
	        tx.commit();
	        return true; // Trả về true nếu thành công
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e; // Ném lại ngoại lệ để controller xử lý
	    } finally {
	        session.close();
	    }
	}
	@Override
	public List<Images> selectByPostID(Integer PostID) {
		Session session = sf.openSession();
	    List<Images> images = null;
	    try {
	        session.beginTransaction();
	        images = session.createQuery("FROM Images WHERE post.PostID = :PostID", Images.class)
	                        .setParameter("PostID", PostID)
	                        .list();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    return images;
	}
	@Override
	public Images selectById(Integer imageID) {
		 Session session = sf.openSession();
		    Images image = null;
		    try {
		        session.beginTransaction();
		        image = session.get(Images.class, imageID);
		        session.getTransaction().commit();
		    } catch (Exception e) {
		        session.getTransaction().rollback();
		        throw e;
		    } finally {
		        session.close();
		    }
		    return image;
	}
	@Override
	public boolean delete(Images image) {
		Session session = sf.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        session.delete(image);
	        tx.commit();
	        return true; // Trả về true nếu xóa thành công
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e; // Ném lại ngoại lệ để xử lý bên ngoài
	    } finally {
	        session.close();
	    }
	}

}
