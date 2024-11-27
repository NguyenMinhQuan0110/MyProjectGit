package com.haha.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.haha.dao.ICommentsDao;
import com.haha.dao.IImagesDao;
import com.haha.dao.ILikePortsDao;
import com.haha.dao.IPostsDAO;
import com.haha.dao.IUsersDAO;
import com.haha.dao.ImplCommentsDao;
import com.haha.dao.ImplImagesDao;
import com.haha.dao.ImplLikePortsDao;
import com.haha.dao.ImplPostsDao;
import com.haha.dao.ImplUsersDao;
import com.haha.entity.Comments;
import com.haha.entity.Images;
import com.haha.entity.LikePorts;
import com.haha.entity.Posts;
import com.haha.entity.Users;



@Controller
@RequestMapping("/posts")
public class PostsController {
	@RequestMapping("/postsofuser")
	public String index(Model m,HttpSession session) {
		 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
		IPostsDAO postDao = new ImplPostsDao();
		List<Posts> lst=postDao.selectAll();
		 // Lấy hình ảnh cho từng bài đăng
	    IImagesDao imagesDao = new ImplImagesDao();
	    for (Posts post : lst) {
	        List<Images> images = imagesDao.selectByPostID(post.getPostID());
	        post.setListImage(new HashSet<>(images)); // Thiết lập danh sách hình ảnh cho bài đăng
	    }
	    // Kiểm tra trạng thái Like cho từng bài viết của người dùng
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    Integer userID = (loggedInUser != null) ? loggedInUser.getUserID() : null;
	    ILikePortsDao likePortsDao = new ImplLikePortsDao();
	    ICommentsDao commentsDao=new ImplCommentsDao();
	    
	 // Lưu thông tin trạng thái Like vào một danh sách tạm
	    Map<Integer, Boolean> postLikesMap = new HashMap<>();
	    Map<Integer, Integer> likeCountsMap = new HashMap<>(); // Bản đồ để lưu số lượt like cho mỗi bài đăng
	    Map<Integer, Integer> commentCountsMap = new HashMap<>();// Bản đồ để lưu số lượt bình luận cho mỗi bài đăng
	    for (Posts post : lst) {
	     // kiểm tra trạng thái like của người dùng
	        boolean isLiked = likePortsDao.findByPostAndUser(post.getPostID(), userID) != null;
	        postLikesMap.put(post.getPostID(), isLiked); // Lưu trạng thái "Like" tạm thời
	        
	     // Đếm số lượt like của bài đăng
	        List<LikePorts> likes = likePortsDao.selectByPostID(post.getPostID());
	        likeCountsMap.put(post.getPostID(), likes.size()); // Lưu số lượt like vào bản đồ
	       // Đếm số lượt like của bài đăng
	        List<Comments> comments = commentsDao.selectByPostID(post.getPostID());
	        commentCountsMap.put(post.getPostID(), comments.size()); // Lưu số lượt like vào bản đồ
	    }
	    System.out.println(likeCountsMap);

	    m.addAttribute("lst", lst);
	    m.addAttribute("postLikesMap", postLikesMap); // Truyền bản đồ trạng thái Like vào model
	    m.addAttribute("likeCountsMap", likeCountsMap); // Truyền số lượt like vào model
	    m.addAttribute("commentCountsMap", commentCountsMap); // Truyền số lượt bình luận vào model
	    return "posts/postsofuser"; // Trả về trang hiển thị bài viết của người dùng
	}
	@PostMapping("/edit")
    public String updatePost(@RequestParam("postID") Integer postID,
                             @RequestParam("content") String content,
                             RedirectAttributes redirectAttributes,HttpSession session) {
		Users loggedInUser = (Users) session.getAttribute("loggedInUser");
		if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
        }
        IPostsDAO postDao = new ImplPostsDao();
        Posts post = postDao.selectById(postID);
     // Kiểm tra quyền cập nhật: 
        // - Người dùng phải là minhquan HOẶC 
        // - Là người sở hữu bài đăng (post.getUser().getId().equals(loggedInUser.getId()))
        if (!"minhquan".equals(loggedInUser.getUsername()) && 
            !post.getUser().getUserID().equals(loggedInUser.getUserID())) {
            redirectAttributes.addFlashAttribute("message", "Bạn không có quyền cập nhật bài viết không phải của bạn!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/posts/postsofuser"; // Chuyển hướng về trang danh sách bài đăng
        }

        post.setContent(content); // Cập nhật nội dung bài đăng
        post.setCreatedAt(new Date());
        
        try {
            postDao.update(post); // Lưu thay đổi
            redirectAttributes.addFlashAttribute("message", "Cập nhật bài viết thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật bài viết thất bại!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/posts/postsofuser"; // Trở lại trang danh sách bài đăng
    }
	// Xử lý xóa bài đăng
    @PostMapping("/delete")
    public String deletePost(@RequestParam("postID") Integer postID,
                             RedirectAttributes redirectAttributes,HttpSession session) {
    	 Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    	if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
        }
        IPostsDAO postDao = new ImplPostsDao();
        IImagesDao imageDao = new ImplImagesDao();
        ILikePortsDao likePortsDao=new ImplLikePortsDao();
        ICommentsDao commentsDao=new ImplCommentsDao();
        Posts post = postDao.selectById(postID);
        if (!"minhquan".equals(loggedInUser.getUsername()) && 
                !post.getUser().getUserID().equals(loggedInUser.getUserID())) {
                redirectAttributes.addFlashAttribute("message", "Bạn không có quyền xóa bài viết không phải của bạn!");
                redirectAttributes.addFlashAttribute("messageType", "danger");
                return "redirect:/posts/postsofuser"; // Chuyển hướng về trang danh sách bài đăng
            }
        try {
        	// Lấy danh sách ảnh liên quan đến bài đăng
            List<Images> imagesList  = imageDao.selectByPostID(postID);
            List<LikePorts> likePortsList=likePortsDao.selectByPostID(postID);
            List<Comments> commentsList=commentsDao.selectByPostID(postID);
            // Xóa các ảnh liên quan
            for (Images image : imagesList) {
                imageDao.delete(image); // Xóa ảnh
            }
            for (LikePorts likePort : likePortsList) {
				likePortsDao.delete(likePort);
			}
            for (Comments comment : commentsList) {
				commentsDao.delete(comment);
			}
            postDao.delete(postID); // Xóa bài đăng
            redirectAttributes.addFlashAttribute("message", "Xóa bài viết thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Xóa bài viết thất bại!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }

        return "redirect:/posts/postsofuser"; // Trở lại trang danh sách bài đăng
    }
    @RequestMapping("/{PostID}")
    public String chitietPost (@PathVariable("PostID") Integer postID, Model model,HttpSession session) {
    	 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
    	// Lấy bài đăng từ DAO
    	IPostsDAO postsDAO = new ImplPostsDao();
    	Posts post = postsDAO.selectById(postID);
    	ICommentsDao commentsDao=new ImplCommentsDao();
    	List<Comments> lst=commentsDao.selectByPostID(postID);
    	// Lấy thông tin like từ DAO
    	ILikePortsDao likePortsDao = new ImplLikePortsDao();

    	// Lấy số lượng lượt thích
    	List<LikePorts> likes = likePortsDao.selectByPostID(postID);
    	int likeCount = likes.size(); // Kích thước danh sách chính là số lượt thích
    	// Lấy số lượng lượt bình luận
    	List<Comments> comments = commentsDao.selectByPostID(postID);
    	int commentCount = comments.size(); // Kích thước danh sách chính là số lượt bình luận
    	// Kiểm tra trạng thái thích của người dùng
    	Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    	boolean liked = likePortsDao.findByPostAndUser(postID, loggedInUser.getUserID()) != null;

    	// Đưa dữ liệu vào model
    	model.addAttribute("lst", lst);
    	model.addAttribute("post", post);
    	model.addAttribute("likeCount", likeCount);
    	model.addAttribute("commentCount", commentCount);
    	model.addAttribute("liked", liked);
    	return"posts/detail";
    }
    @PostMapping("/addbl")
    public String insertBL(@RequestParam("postID") Integer postID,
            @RequestParam("content") String content,
            HttpSession session,
            RedirectAttributes redirectAttributes){
    	// Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
        }
        // Lấy thông tin người dùng hiện tại
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        // Tạo comment mới
        Comments comment = new Comments();
        comment.setContent(content);
        comment.setCreatedAt(new Date());
        comment.setUser(loggedInUser);
        comment.setPost(new Posts(postID, null, null, null));

        // Thêm vào cơ sở dữ liệu thông qua DAO
        ICommentsDao commentsDao = new ImplCommentsDao();
        try {
        	commentsDao.insert(comment);
        	redirectAttributes.addFlashAttribute("message", "Bình luận thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "Bình luận thất bại!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
		}
     // Chuyển hướng tới trang chi tiết bài viết bằng phương thức GET
        return "redirect:/posts/" + postID;
    }
    @PostMapping("/deleteBL")
    public String deleteBL(@RequestParam("commentID") Integer commentID, 
            @RequestParam("postID") Integer postID, 
            HttpSession session,RedirectAttributes redirectAttributes ) {
    	// Kiểm tra người dùng đã đăng nhập
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/index"; // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
        }
        ICommentsDao commentsDao = new ImplCommentsDao();
        Comments comment = commentsDao.selectById(commentID); // Lấy thông tin bình luận
        if (comment == null || (!"minhquan".equals(loggedInUser.getUsername()) && 
                !comment.getUser().getUserID().equals(loggedInUser.getUserID()))) {
			redirectAttributes.addFlashAttribute("message", "Bạn không có quyền xóa bình luận này!");
			redirectAttributes.addFlashAttribute("messageType", "danger");
			return "redirect:/posts/" + postID; // Quay lại trang bài viết
			}
        try {
            commentsDao.delete(comment);
            redirectAttributes.addFlashAttribute("message", "Xóa bình luận thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "Xóa bình luận thất bại!");
            redirectAttributes.addFlashAttribute("messageType", "danger");
		}
 
        return "redirect:/posts/" + postID;
    }
    @RequestMapping("/profile/{UserID}")
    public String profile(@PathVariable("UserID") Integer UserID, Model m,HttpSession session) {
    	//kiểm tra người dùng đăng nhập chưa
    	if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
        }
    	IPostsDAO postDao = new ImplPostsDao();
		List<Posts> lst=postDao.selectByUserID(UserID);
		 // Lấy hình ảnh cho từng bài đăng
	    IImagesDao imagesDao = new ImplImagesDao();
	    for (Posts post : lst) {
	        List<Images> images = imagesDao.selectByPostID(post.getPostID());
	        post.setListImage(new HashSet<>(images)); // Thiết lập danh sách hình ảnh cho bài đăng
	    }
	    // Kiểm tra trạng thái Like cho từng bài viết của người dùng
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    Integer userID = (loggedInUser != null) ? loggedInUser.getUserID() : null;
	    ILikePortsDao likePortsDao = new ImplLikePortsDao();
	    ICommentsDao commentsDao=new ImplCommentsDao();
	    IUsersDAO userDAO=new ImplUsersDao();
	    Users user=userDAO.selectById(UserID);
	 // Lưu thông tin trạng thái Like vào một danh sách tạm
	    Map<Integer, Boolean> postLikesMap = new HashMap<>();
	    Map<Integer, Integer> likeCountsMap = new HashMap<>(); // Bản đồ để lưu số lượt like cho mỗi bài đăng
	    Map<Integer, Integer> commentCountsMap = new HashMap<>();// Bản đồ để lưu số lượt bình luận cho mỗi bài đăng
	    for (Posts post : lst) {
	     // kiểm tra trạng thái like của người dùng
	        boolean isLiked = likePortsDao.findByPostAndUser(post.getPostID(), userID) != null;
	        postLikesMap.put(post.getPostID(), isLiked); // Lưu trạng thái "Like" tạm thời
	        
	     // Đếm số lượt like của bài đăng
	        List<LikePorts> likes = likePortsDao.selectByPostID(post.getPostID());
	        likeCountsMap.put(post.getPostID(), likes.size()); // Lưu số lượt like vào bản đồ
	       // Đếm số lượt like của bài đăng
	        List<Comments> comments = commentsDao.selectByPostID(post.getPostID());
	        commentCountsMap.put(post.getPostID(), comments.size()); // Lưu số lượt like vào bản đồ
	    }
	    
	    System.out.println(likeCountsMap);
	    m.addAttribute("user", user);
	    m.addAttribute("lst", lst);
	    m.addAttribute("postLikesMap", postLikesMap); // Truyền bản đồ trạng thái Like vào model
	    m.addAttribute("likeCountsMap", likeCountsMap); // Truyền số lượt like vào model
	    m.addAttribute("commentCountsMap", commentCountsMap); // Truyền số lượt bình luận vào model
    	return "posts/profile";
    } 
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model,HttpSession session) {
    	//kiểm tra người dùng đăng nhập chưa
    	if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
        }
    	Users loggedInUser = (Users) session.getAttribute("loggedInUser");
    	 Integer userID = (loggedInUser != null) ? loggedInUser.getUserID() : null;
        // Tìm kiếm bài viết theo content
        IPostsDAO postDao = new ImplPostsDao();
        List<Posts> posts = postDao.searchByContent(keyword);

        // Tìm kiếm người dùng theo username
        IUsersDAO userDao = new ImplUsersDao();
        List<Users> users = userDao.searchByKeyword(keyword);
        ILikePortsDao likePortsDao = new ImplLikePortsDao();
	    ICommentsDao commentsDao=new ImplCommentsDao();
        // Lưu thông tin trạng thái Like vào một danh sách tạm
	    Map<Integer, Boolean> postLikesMap = new HashMap<>();
	    Map<Integer, Integer> likeCountsMap = new HashMap<>(); // Bản đồ để lưu số lượt like cho mỗi bài đăng
	    Map<Integer, Integer> commentCountsMap = new HashMap<>();// Bản đồ để lưu số lượt bình luận cho mỗi bài đăng
	    for (Posts post : posts) {
	     // kiểm tra trạng thái like của người dùng
	        boolean isLiked = likePortsDao.findByPostAndUser(post.getPostID(), userID) != null;
	        postLikesMap.put(post.getPostID(), isLiked); // Lưu trạng thái "Like" tạm thời
	        
	     // Đếm số lượt like của bài đăng
	        List<LikePorts> likes = likePortsDao.selectByPostID(post.getPostID());
	        likeCountsMap.put(post.getPostID(), likes.size()); // Lưu số lượt like vào bản đồ
	       // Đếm số lượt like của bài đăng
	        List<Comments> comments = commentsDao.selectByPostID(post.getPostID());
	        commentCountsMap.put(post.getPostID(), comments.size()); // Lưu số lượt like vào bản đồ
	    }

        // Thêm kết quả vào model để truyền vào JSP
        model.addAttribute("posts", posts);
        model.addAttribute("users", users);
        model.addAttribute("postLikesMap", postLikesMap); // Truyền bản đồ trạng thái Like vào model
	    model.addAttribute("likeCountsMap", likeCountsMap); // Truyền số lượt like vào model
	    model.addAttribute("commentCountsMap", commentCountsMap); // Truyền số lượt bình luận vào model

        return "posts/seach"; // Chuyển đến trang JSP hiển thị kết quả tìm kiếm
    }
}
