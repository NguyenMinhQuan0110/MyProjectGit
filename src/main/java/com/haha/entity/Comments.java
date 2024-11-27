package com.haha.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CommentID")
	private Integer CommentID;
	@ManyToOne
	@JoinColumn(name ="PostID")
	private Posts post;
	@ManyToOne
	@JoinColumn(name ="UserID")
	private Users user;
	@Column(name = "Content")
	private String Content;
	@Column(name = "CreatedAt")
	private Date CreatedAt;
	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comments(Integer commentID, Posts post, Users user, String content, Date createdAt) {
		super();
		CommentID = commentID;
		this.post = post;
		this.user = user;
		Content = content;
		CreatedAt = createdAt;
	}
	public Integer getCommentID() {
		return CommentID;
	}
	public void setCommentID(Integer commentID) {
		CommentID = commentID;
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
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Date getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
	
}
