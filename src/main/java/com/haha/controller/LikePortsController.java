package com.haha.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.haha.dao.ILikePortsDao;

import com.haha.dao.ImplLikePortsDao;

import com.haha.entity.LikePorts;
import com.haha.entity.Posts;
import com.haha.entity.Users;

@Controller
@RequestMapping("likeport")
public class LikePortsController {
	@PostMapping("/toggle")
	@ResponseBody
	public String toggleLike(@RequestParam("postID") Integer postID, HttpSession session) {
	    // Kiểm tra người dùng đăng nhập
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "{\"success\": false, \"message\": \"Please log in\"}";
	    }

	    Integer userID = loggedInUser.getUserID();
	    ILikePortsDao likePortsDao = new ImplLikePortsDao();
	    //kiểm tra xem người dùng đã like chưa
	    LikePorts existingLike = likePortsDao.findByPostAndUser(postID, userID);

	    boolean liked = false;
	    if (existingLike == null) {
	        // Nếu chưa like, thêm mới
	        LikePorts newLike = new LikePorts();
	        newLike.setPost(new Posts(postID, null, null, null));
	        newLike.setUser(new Users(userID, null, null, null, null, null, null));
	        likePortsDao.insert(newLike);
	        liked = true;
	    } else {
	        // Nếu đã like, xóa like
	        likePortsDao.delete(existingLike);
	        liked = false;
	    }
	 // Lấy lại số lượt like của bài đăng
        List<LikePorts> likeList = likePortsDao.selectByPostID(postID);
        int likeCount = likeList.size();

	    // Trả về phản hồi JSON
        return "{\"success\": true, \"liked\": " + liked + ", \"likeCount\": " + likeCount + "}";
	}

	
}
