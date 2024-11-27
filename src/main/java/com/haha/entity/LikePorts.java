package com.haha.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LikePorts")
public class LikePorts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LikeID")
	private Integer LikeID;
	@ManyToOne
	@JoinColumn(name ="PostID")
	private Posts post;
	@ManyToOne
	@JoinColumn(name ="UserID")
	private Users user;
	public LikePorts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LikePorts(Integer likeID, Posts post, Users user) {
		super();
		LikeID = likeID;
		this.post = post;
		this.user = user;
	}
	public Integer getLikeID() {
		return LikeID;
	}
	public void setLikeID(Integer likeID) {
		LikeID = likeID;
	}
	public Posts getPost() {
		return post;
	}
	public void setPost(Posts post) {
		this.post = post;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	

}
