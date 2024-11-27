package com.haha.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.haha.entity.Users;

public class ImplUsersDao implements IUsersDAO{

	private SessionFactory sf;
	
	public ImplUsersDao() {
		sf=HibernateUtils.getSessionFactory();
	}
	
	@Override
	public List<Users> selectAll() {
		Session ss=sf.openSession(); //Mở  kết nói DB 
		List<Users>lst=new ArrayList<Users>();
		try {
			// Mở phiên làm việc
			ss.beginTransaction();
			//Thực hiện lệnh/giao dịch
			
			lst=ss.createQuery("from Users").list();
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
	public boolean insert(Users user) {
	    Session session = sf.openSession();
	    Transaction tx =null;
	    try {
	        tx = session.beginTransaction();
	        session.save(user);
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
	public Users selectById(Integer userId) {
		 Session ss = sf.openSession();
		    Users user = null;
		    try {
		        user = ss.get(Users.class, userId); // Lấy người dùng theo ID
		    } catch (Exception e) {
		        System.out.println("Lỗi " + e.getMessage());
		    } finally {
		        ss.close();
		    }
		    return user;
	}

	@Override
	public boolean update(Users user) {
		Session ss = sf.openSession();
		Transaction tx = null;
	    try {
	        tx = ss.beginTransaction();
	        ss.update(user); // Cập nhật thông tin người dùng
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
	public void delete(Integer userId) {
		 Session session = sf.openSession();
		    Transaction tx = null;
		    try {
		        tx = session.beginTransaction();
		        Users user = session.get(Users.class, userId); // Tìm user theo ID
		        if (user != null) {
		            session.delete(user); // Xóa user nếu tồn tại
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
	public List<Users> searchByKeyword(String keyword) {
		Session session = sf.openSession();
	    Transaction tx = null;
	    List<Users> users = null;
	    
	    try {
	        tx = session.beginTransaction();
	        String hql = "FROM Users u WHERE u.Username LIKE :keyword OR u.Email LIKE :keyword";
	        
	        // Tham số hóa Query với kiểu Users
	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("keyword", "%" + keyword + "%");
	        
	        users = query.list();
	        tx.commit();
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        throw e;
	    } finally {
	        session.close();
	    }
	    
	    return users;
	}

	@Override
	public Users findByEmail(String email) {
		Session session = sf.openSession();
        String hql = "FROM Users u WHERE u.Email = :email";
        Query<Users> query = session.createQuery(hql, Users.class);
        query.setParameter("email", email);
        Users user = query.uniqueResult();
        session.close();
        return user;
	}

	@Override
	public Users findByOtp(String otp) {
		Session session = sf.openSession();
        String hql = "FROM Users u WHERE u.Otp = :otp";
        Query<Users> query = session.createQuery(hql, Users.class);
        query.setParameter("otp", otp);
        Users user = query.uniqueResult();
        session.close();
        return user;
	}

}
