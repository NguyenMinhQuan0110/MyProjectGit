package com.haha.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.haha.entity.Comments;

public class ImplCommentsDao implements ICommentsDao {

private SessionFactory sf;
	
	public ImplCommentsDao() {
		sf=HibernateUtils.getSessionFactory();
	}
	@Override
	public boolean insert(Comments comment) {
		Session session = sf.openSession();
	    Transaction tx =null;
	    try {
	        tx = session.beginTransaction();
	        session.save(comment);
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
	public List<Comments> selectByPostID(Integer PostID) {
		Session session = sf.openSession();
	    List<Comments> comments = null;
	    try {
	        session.beginTransaction();
	        comments = session.createQuery("FROM Comments WHERE post.PostID = :PostID ORDER BY createdAt DESC", Comments.class)
	                        .setParameter("PostID", PostID)
	                        .list();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    return comments;
	}

	@Override
	public Comments selectById(Integer commentID) {
		Session session = sf.openSession();
	    Comments comments = null;
	    try {
	        session.beginTransaction();
	        comments = session.get(Comments.class, commentID);
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    return comments;
	}

	@Override
	public boolean delete(Comments comment) {
		Session session = sf.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        session.delete(comment);
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
