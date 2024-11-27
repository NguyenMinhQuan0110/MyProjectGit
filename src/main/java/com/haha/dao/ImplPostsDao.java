package com.haha.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.haha.entity.Images;
import com.haha.entity.Posts;



public class ImplPostsDao implements IPostsDAO {
	private SessionFactory sf;
	
	public ImplPostsDao() {
		sf=HibernateUtils.getSessionFactory();
	}

	@Override
	public List<Posts> selectAll() {
		Session ss=sf.openSession(); //Mở  kết nói DB 
		List<Posts>lst=new ArrayList<Posts>();
		try {
			// Mở phiên làm việc
			ss.beginTransaction();
			//Thực hiện lệnh/giao dịch
			
			lst = ss.createQuery("FROM Posts ORDER BY createdAt DESC", Posts.class).list();
			//Xác nhận giao dịch
			ss.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Lỗi "+e.getMessage());
			ss.getTransaction().rollback();//Hoàn tác lệnh		
		}finally {
			ss.close();
		}
		return lst;
	}

	@Override 
	public boolean insert(Posts posts) {
		 Session session = sf.openSession();
		    Transaction tx =null;
		    try {
		        tx = session.beginTransaction();
		        session.save(posts);
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
	public Posts selectById(Integer postID) {
		Session ss=sf.openSession();
		Posts posts=null;
		try {
			posts=ss.get(Posts.class,postID);
		} catch (Exception e) {
			System.out.println("Lỗi " + e.getMessage());
		}finally {
			ss.close();
		}
		return posts;
	}

	@Override
	public boolean update(Posts posts) {
		Session ss = sf.openSession();
		Transaction tx = null;
	    try {
	        tx = ss.beginTransaction();
	        ss.update(posts); // Cập nhật thông tin bài đăng
	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e; // Ném lại ngoại lệ để controller xử lý
	    } finally {
	        ss.close();
	    }
	}
	@Override
	public void delete(Integer postID) {
		Session session = sf.openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        Posts posts = session.get(Posts.class, postID); // Tìm post theo ID
	        if (posts != null) {
	            session.delete(posts); // Xóa post nếu tồn tại
	        }
	        tx.commit(); // Commit giao dịch
	    } catch (Exception e) {
	        if (tx != null) {
	            tx.rollback(); // Rollback nếu có lỗi
	        }
	        e.printStackTrace();
	    } finally {
	        session.close(); // Đảm bảo đóng session
	    }
	}

	@Override
	public List<Posts> selectByUserID(Integer UserID) {
		Session session = sf.openSession();
	    List<Posts> posts = null;
	    try {
	        session.beginTransaction();
	        posts = session.createQuery("FROM Posts WHERE user.UserID = :UserID ORDER BY createdAt DESC", Posts.class)
	                        .setParameter("UserID", UserID)
	                        .list();
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    return posts;
	}

	@Override
	public List<Posts> searchByContent(String keyword) {
		 Session session = sf.openSession();
		    List<Posts> posts = null;
		    try {
		        session.beginTransaction();
		        // Sử dụng câu lệnh HQL với toán tử LIKE để tìm kiếm theo từ khóa
		        posts = session.createQuery("FROM Posts WHERE content LIKE :keyword ORDER BY createdAt DESC", Posts.class)
		                       .setParameter("keyword", "%" + keyword + "%")
		                       .list();
		        session.getTransaction().commit();
		    } catch (Exception e) {
		        session.getTransaction().rollback();
		        throw e;
		    } finally {
		        session.close();
		    }
		    return posts;
	}

	
}
