package com.haha.dao;

import java.util.List;

import com.haha.entity.Users;

public interface IUsersDAO {
	public List<Users> selectAll();
	public boolean insert(Users user); // Thêm phương thức insert
	public Users selectById(Integer userId);// Thêm phương thức selectById
	public boolean update(Users user); // Thêm phương thức update
	public void delete(Integer userId);
	public List<Users> searchByKeyword(String keyword);
	public Users findByEmail(String email); // Phương thức tìm người dùng qua email
	public Users findByOtp(String otp);

}
