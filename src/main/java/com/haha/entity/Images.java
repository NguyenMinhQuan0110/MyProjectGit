package com.haha.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "Images")
public class Images {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="imageID")
	private Integer imageID;
	@Column(name = "imageAd")
	private String imageAd;
	@ManyToOne
	@JoinColumn(name ="PostID")
	private Posts post;
	@ManyToOne
	@JoinColumn(name ="UserID")
	private Users user;
	@Transient
	private MultipartFile fileAnh;
	public Images() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Images(Integer imageID, String imageAd, Posts post, Users user, MultipartFile fileAnh) {
		super();
		this.imageID = imageID;
		this.imageAd = imageAd;
		this.post = post;
		this.user = user;
		this.fileAnh = fileAnh;
	}
	public Integer getImageID() {
		return imageID;
	}
	public void setImageID(Integer imageID) {
		this.imageID = imageID;
	}
	public String getImageAd() {
		return imageAd;
	}
	public void setImageAd(String imageAd) {
		this.imageAd = imageAd;
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
	public MultipartFile getFileAnh() {
		return fileAnh;
	}
	public void setFileAnh(MultipartFile fileAnh) {
		this.fileAnh = fileAnh;
	}
	
}
