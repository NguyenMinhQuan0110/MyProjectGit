package com.haha.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID")
	private Integer UserID;
	@Column(name = "Username")
    private String Username;
	@Column(name = "Password")
    private String Password;
	@Column(name = "Email")
    private String Email;
	@Column(name = "CreatedAt")
    private Date CreatedAt;
	@Column(name = "Otp")
	private String Otp;
	@Column(name = "OtpCreatedAt")
	private Date OtpCreatedAt;
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private List<Posts> Posts;
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private Set<Images> listImage; 
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private Set<LikePorts> listLikePorts;
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private Set<Comments> listComments;
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Users(Integer userID, String username, String password, String email, Date createdAt,String otp,Date otpCreatedAt) {
		super();
		UserID = userID;
		Username = username;
		Password = password;
		Email = email;
		CreatedAt = createdAt;
		Otp=otp;
		OtpCreatedAt=otpCreatedAt;
	}
	public Integer getUserID() {
		return UserID;
	}
	public void setUserID(Integer userID) {
		UserID = userID;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public Date getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
	public String getOtp() {
		return Otp;
	}
	public void setOtp(String otp) {
		Otp = otp;
	}
	public Date getOtpCreatedAt() {
		return OtpCreatedAt;
	}
	public void setOtpCreatedAt(Date otpCreatedAt) {
		OtpCreatedAt = otpCreatedAt;
	}
	public List<Posts> getPosts() {
		return Posts;
	}
	public void setPosts(List<Posts> posts) {
		Posts = posts;
	}
	public Set<Images> getListImage() {
		return listImage;
	}
	public void setListImage(Set<Images> listImage) {
		this.listImage = listImage;
	}
	public Set<LikePorts> getListLikePorts() {
		return listLikePorts;
	}
	public void setListLikePorts(Set<LikePorts> listLikePorts) {
		this.listLikePorts = listLikePorts;
	}
	public Set<Comments> getListComments() {
		return listComments;
	}
	public void setListComments(Set<Comments> listComments) {
		this.listComments = listComments;
	}
	
}
