package com.haha.dao;

import java.util.List;

import com.haha.entity.LikePorts;

public interface ILikePortsDao {
	public boolean insert (LikePorts likePorts);
	public List<LikePorts> selectByPostID (Integer PostID);
	public boolean delete(LikePorts likePorts);
	public LikePorts findByPostAndUser(Integer postID, Integer userID);//tìm kiến trạng thái like theo postID và userID
}
