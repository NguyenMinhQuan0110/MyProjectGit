package com.haha.dao;

import java.util.List;

import com.haha.entity.Comments;

public interface ICommentsDao {
	public boolean insert(Comments comment);
	public List<Comments> selectByPostID(Integer PostID);
	public Comments selectById(Integer commentID); 
	public boolean delete(Comments comment);
	
}
