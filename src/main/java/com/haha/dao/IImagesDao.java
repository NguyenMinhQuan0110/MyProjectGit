package com.haha.dao;

import java.util.List;

import com.haha.entity.Images;

public interface IImagesDao {
	
	public boolean insert(Images image);
	public List<Images> selectByPostID(Integer PostID);
	public Images selectById(Integer imageID);
	public boolean delete(Images image);
}
