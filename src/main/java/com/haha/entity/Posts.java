package com.haha.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Posts")
public class Posts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PostID")
	private Integer PostID;
	@ManyToOne
	@JoinColumn(name = "UserID",referencedColumnName = "UserID")
    private Users user;
    @Column(name = "Content")
    private String Content;
    @Column(name = "CreatedAt")
    private Date CreatedAt;
    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
	private Set<Images> listImage; 
    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private Set<LikePorts> listLikePorts;
    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private Set<Comments> listComments;
	public Posts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Posts(Integer postID, Users user, String content, Date createdAt) {
		super();
		PostID = postID;
		this.user = user;
		Content = content;
		CreatedAt = createdAt;
	}
	public Integer getPostID() {
		return PostID;
	}
	public void setPostID(Integer postID) {
		PostID = postID;
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
