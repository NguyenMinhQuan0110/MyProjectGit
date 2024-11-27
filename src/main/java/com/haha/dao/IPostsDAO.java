package com.haha.dao;

import java.util.List;

import com.haha.entity.Posts;



public interface IPostsDAO {
	public List<Posts> selectAll();
	public boolean insert(Posts posts);
	public Posts selectById(Integer postID);
	public boolean update(Posts posts);
	public void delete(Integer postID);
	public List<Posts> selectByUserID(Integer UserID);
	public List<Posts> searchByContent(String keyword);
}
