package com.haha.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.haha.entity.LikePorts;

public class ImplLikePortsDao implements ILikePortsDao {
	
	private SessionFactory sf;
	
	public ImplLikePortsDao() {
		sf=HibernateUtils.getSessionFactory();
	}
	
	@Override
	public boolean insert(LikePorts likePorts) {
		Session session = sf.openSession();
	    Transaction tx =null;
	    try {
	        tx = session.beginTransaction();
	        session.save(likePorts);
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
	public List<LikePorts> selectByPostID(Integer PostID) {
		Session session = sf.openSession();
	    List<LikePorts> likePorts = null;
	    try {
	        session.beginTransaction();
	        likePorts = session.createQuery("FROM LikePorts WHERE post.PostID = :PostID", LikePorts.class)
	                        .setParameter("PostID", PostID)
	                        .list();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    return likePorts;
	}

	@Override
	public boolean delete(LikePorts likePorts) {
		Session session = sf.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        session.delete(likePorts);
	        tx.commit();
	        return true; // Trả về true nếu xóa thành công
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e; // Ném lại ngoại lệ để xử lý bên ngoài
	    } finally {
	        session.close();
	    }
	}

	@Override
	public LikePorts findByPostAndUser(Integer postID, Integer userID) {
		 Session session = sf.openSession();
		    LikePorts likePort = null;
		    try {
		        session.beginTransaction();
		        likePort = session.createQuery(
		            "FROM LikePorts WHERE post.PostID = :postID AND user.UserID = :userID",
		            LikePorts.class
		        )
		        .setParameter("postID", postID)
		        .setParameter("userID", userID)
		        .uniqueResult();
		        session.getTransaction().commit();
		    } catch (Exception e) {
		        session.getTransaction().rollback();
		        throw e;
		    } finally {
		        session.close();
		    }
		    return likePort;
	}

}
