package com.haha.controller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.haha.dao.IImagesDao;
import com.haha.dao.IPostsDAO;
import com.haha.dao.IUsersDAO;
import com.haha.dao.ImplImagesDao;
import com.haha.dao.ImplPostsDao;
import com.haha.dao.ImplUsersDao;
import com.haha.entity.Images;
import com.haha.entity.Posts;
import com.haha.entity.Users;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@RequestMapping(value = {"","/","/list"})
	public String index(Model m, HttpSession session) {
	    // Kiểm tra người dùng đã đăng nhập hay chưa
	    Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "redirect:/index"; // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
	    }

	    // Kiểm tra Username của người dùng
	    if ("minhquan".equals(loggedInUser.getUsername())) {
	        IUsersDAO userDao = new ImplUsersDao();
	        List<Users> lst = userDao.selectAll();
	        m.addAttribute("lst", lst);
	        return "users/index"; // Chỉ trả về trang users/index nếu Username là minhquan
	    }

	    // Trường hợp không phải minhquan, chuyển hướng đến posts/postsofuser
	    return "redirect:/posts/postsofuser";
	}
	@PostMapping("/add")
	public String addUser(String username, String email, String password,String otp,Date otpCreatedAt, RedirectAttributes redirectAttributes,HttpSession session) {
		 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
	    Users newUser = new Users();
	    newUser.setUsername(username);
	    newUser.setEmail(email);    
	 // Mã hóa mật khẩu
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(password);
	    newUser.setPassword(encodedPassword);
	    newUser.setCreatedAt(new Date());
	    newUser.setOtp(otp);
	    newUser.setOtpCreatedAt(otpCreatedAt);
	    

	    IUsersDAO userDao = new ImplUsersDao();
	    try {
	        // Gọi phương thức để lưu người dùng vào CSDL
	        userDao.insert(newUser); // Không cần biến isSuccess
	        redirectAttributes.addFlashAttribute("message", "Thêm thành công!");
	        redirectAttributes.addFlashAttribute("messageType", "success");
	    } catch (Exception e) {
	        // Chắc chắn rằng bạn lấy được thông điệp lỗi
	        redirectAttributes.addFlashAttribute("message", "Tên người dùng có thể đã tồn tại");
	        redirectAttributes.addFlashAttribute("messageType", "danger");
	    }

	    return "redirect:/users"; // Chuyển hướng về trang danh sách người dùng
	}


	 @GetMapping("/initedit")
	 public String showEditForm(Integer userId, Model model,HttpSession session) {
		 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
		 IUsersDAO userDao=new ImplUsersDao();
	     Users user = userDao.selectById(userId);
	     model.addAttribute("user", user);
	     return "users/edit"; // Trả về view chứa form sửa
	 }
	 @PostMapping("/edit")
	 public String editUser(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
		 IUsersDAO userDao=new ImplUsersDao();
		// Kiểm tra nếu mật khẩu trống để không cập nhật mật khẩu
		    if (user.getPassword() == null || user.getPassword().isEmpty()) {
		        Users existingUser = userDao.selectById(user.getUserID());
		        user.setPassword(existingUser.getPassword()); // Giữ nguyên mật khẩu cũ
		    }
		    // Mã hóa mật khẩu
		    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    String encodedPassword = passwordEncoder.encode(user.getPassword());
		    user.setPassword(encodedPassword);
		    user.setCreatedAt(new Date());
		    user.setOtp(null);
		    user.setOtpCreatedAt(null);
		    try {
		        userDao.update(user); // Cập nhật thông tin người dùng
		        redirectAttributes.addFlashAttribute("message", "Sửa thành công!");
		        redirectAttributes.addFlashAttribute("messageType", "success");
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("message", "Sửa thất bại!");
		        redirectAttributes.addFlashAttribute("messageType", "danger");
		    }
	     return "redirect:/users"; // Trở về trang danh sách người dùngs
	 }
	 @PostMapping("/delete")
	 public String deleteUser(Integer userId, RedirectAttributes redirectAttributes,HttpSession session) {
		 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
	     IUsersDAO userDao = new ImplUsersDao();
	     try {
	         // Gọi phương thức để xóa người dùng
	         userDao.delete(userId);
	         // Thêm thông báo thành công vào RedirectAttributes
	         redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
	         redirectAttributes.addFlashAttribute("messageType", "success");
	     } catch (Exception e) {
	         // Thêm thông báo thất bại vào RedirectAttributes
	         redirectAttributes.addFlashAttribute("message", "Xóa thất bại!");
	         redirectAttributes.addFlashAttribute("messageType", "danger");
	     }

	     return "redirect:/users"; // Chuyển hướng về trang danh sách người dùng
	 }
	 @GetMapping("/search")
	 public String searchUsers(String keyword, Model model,HttpSession session) {
		 if (session.getAttribute("loggedInUser") == null) {
	            return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	        }
	     IUsersDAO userDao = new ImplUsersDao();
	     List<Users> lst;
	     
	     if (keyword != null && !keyword.trim().isEmpty()) {
	         lst = userDao.searchByKeyword(keyword);
	     } else {
	         lst = userDao.selectAll(); // Nếu không có từ khóa tìm kiếm, trả về danh sách đầy đủ
	     }
	     
	     model.addAttribute("lst", lst);
	     return "users/index";
	 }
	 @PostMapping("/creatpost")
	 public String creatpost(@ModelAttribute("Posts") Posts posts, HttpSession session, RedirectAttributes redirectAttributes,
	                         @RequestParam("fileAnh") MultipartFile[] fileAnh, HttpServletRequest request) {
	     Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	     if (loggedInUser == null) {
	         return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	     }

	     // Lấy thông tin người dùng từ session và gán vào bài đăng
	     posts.setUser(loggedInUser);
	     posts.setCreatedAt(new Date());

	     IPostsDAO postsDao = new ImplPostsDao();
	     try {
	         // Lưu bài đăng vào cơ sở dữ liệu
	         boolean success = postsDao.insert(posts);

	         // Xử lý lưu nhiều ảnh
	         if (fileAnh != null && fileAnh.length > 0) {
	             for (MultipartFile file : fileAnh) {
	                 if (!file.isEmpty()) {
	                     try {
	                         // Lấy tên tệp gốc
	                         String filePath = file.getOriginalFilename();
	                         if (filePath != null && !filePath.isEmpty()) {
	                             // Lấy đường dẫn đến thư mục resource/img
	                             String uploadRootPath = request.getServletContext().getRealPath("resource" + File.separator + "img");
	                             File uploadRootDir = new File(uploadRootPath);
	                             if (!uploadRootDir.exists()) {
	                                 uploadRootDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
	                             }

	                             File destFile = new File(uploadRootDir, filePath);
	                             System.out.println("Thư mục chứa file: " + uploadRootPath);

	                             // Lưu tập tin
	                             byte[] data = file.getBytes();
	                             Files.write(destFile.toPath(), data, StandardOpenOption.CREATE);
	                             System.out.println("Ghi tập tin thành công");

	                             // Lưu thông tin ảnh vào cơ sở dữ liệu
	                             Images image = new Images();
	                             image.setImageAd("resource/img/" + filePath);
	                             image.setPost(posts);
	                             image.setUser(loggedInUser);
	                             image.setFileAnh(file);

	                             // Gọi DAO để lưu ảnh
	                             IImagesDao imagesDao = new ImplImagesDao();
	                             imagesDao.insert(image);

	                             if (posts.getListImage() == null) {
	                                 posts.setListImage(new HashSet<>());
	                             }
	                             // Thêm ảnh vào danh sách ảnh của bài đăng
	                             posts.getListImage().add(image);
	                         }
	                     } catch (Exception e) {
	                         System.out.println("Lỗi khi xử lý lưu ảnh: " + e.getMessage());
	                         e.printStackTrace();
	                         redirectAttributes.addFlashAttribute("message", "Lỗi khi lưu ảnh!");
	                         redirectAttributes.addFlashAttribute("messageType", "danger");
	                     }
	                 }
	             }
	         }

	         // Thêm thông báo thành công
	         redirectAttributes.addFlashAttribute("message", "Tạo bài viết thành công!");
	         redirectAttributes.addFlashAttribute("messageType", "success");

	     } catch (Exception e) {
	         System.out.println("Lỗi khi tạo bài viết: " + e.getMessage());
	         e.printStackTrace();
	         redirectAttributes.addFlashAttribute("message", "Tạo bài viết thất bại!");
	         redirectAttributes.addFlashAttribute("messageType", "danger");
	     }

	     // Sau khi tạo bài đăng, bạn có thể chuyển hướng về trang hiển thị các bài đăng
	     return "redirect:/posts/postsofuser";
	 }


}
