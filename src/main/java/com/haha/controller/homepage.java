package com.haha.controller;


import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.haha.dao.IUsersDAO;
import com.haha.dao.ImplUsersDao;
import com.haha.entity.Users;
import com.haha.service.EmailService;

@Controller
@RequestMapping("/")
public class homepage {
	@RequestMapping(value = {"","/","/index"})
	public String index() {
		return "index";
	}
	@PostMapping("/login")
    public String login(String email, String password, RedirectAttributes redirectAttributes,HttpSession session) {
	 	IUsersDAO userDao = new ImplUsersDao();
        Users user = userDao.findByEmail(email);
        
        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
            
            if (isPasswordMatch) {
            	// Lưu thông tin người dùng vào session khi đăng nhập thành công
                session.setAttribute("loggedInUser", user);
                redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
             // Kiểm tra Username để điều hướng
                if ("minhquan".equals(user.getUsername())) {
                    return "redirect:/users"; // Điều hướng về trang users/index nếu Username là minhquan
                } else {
                    return "redirect:/posts/postsofuser"; // Điều hướng về trang posts/postsofuser cho các trường hợp khác
                }
            }
        }
        
        // Nếu thất bại, trả về trang login với thông báo lỗi
        redirectAttributes.addFlashAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
        return "redirect:/index";
    }
	@GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Xóa thông tin người dùng khỏi session
        session.invalidate(); // Xóa toàn bộ session
        
        redirectAttributes.addFlashAttribute("message", "Đăng xuất thành công!");
        return "index"; // Chuyển hướng về trang đăng nhập
    }
	
	
	
	//đoạn code cho quyên mật khẩu
	@GetMapping("/email")
	public String initemail() {
		return "email";
	}
	@PostMapping("/sendotp")
	public String sendotp(@RequestParam("email")String email,RedirectAttributes redirectAttributes) {
		IUsersDAO userDao = new ImplUsersDao();
        Users user = userDao.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Email không tồn tại!");
            return "redirect:/email"; // Nếu email không tồn tại, báo lỗi
        }
        // Tạo OTP và lưu vào CSDL
        String otp = OtpUtil.generateOtp(); 
        user.setOtp(otp); 
        user.setOtpCreatedAt(new Date());
        userDao.update(user); // Cập nhật người dùng với OTP
     // Gửi OTP qua email
        String subject = "Mã OTP của bạn";
        String body = "Mã OTP của bạn là: " + otp;
        EmailService.sendEmail(email, subject, body); // Gửi email
		return"otp";
	}
	@GetMapping("/otp")
	public String initotp() {
		return "otp";
	}
	@PostMapping("/checkotp")
	public String checkotp(@RequestParam("otp") String otp,RedirectAttributes redirectAttributes,Model model) {
		IUsersDAO userDao = new ImplUsersDao();
		Users user = userDao.findByOtp(otp); // Tìm người dùng theo OTP
	    if (user == null || otpExpired(user.getOtpCreatedAt())) { // Kiểm tra OTP và thời gian hết hạn
	        redirectAttributes.addFlashAttribute("message", "OTP không hợp lệ hoặc đã hết hạn!");
	        return "redirect:/otp"; 
	    }
	 // Nếu OTP hợp lệ, thêm email vào model để hiển thị trong form đổi mật khẩu
	    model.addAttribute("email", user.getEmail());
	    return "editpassword"; // Chuyển hướng về trang đăng nhập
	}
	private boolean otpExpired(Date otpCreatedAt) {
	    // Kiểm tra xem OTP có quá 5 phút chưa
	    Date now = new Date();
	    long differenceInMinutes = (now.getTime() - otpCreatedAt.getTime()) / (60 * 1000);
	    return differenceInMinutes > 5;
	}
	@GetMapping("/editpassword")
	public String initEditPassword(@ModelAttribute("email") String email, Model model) {
	    if (email == null || email.isEmpty()) {
	        return "redirect:/otp"; // Nếu không có email, chuyển lại trang OTP
	    }
	    model.addAttribute("email", email);
	    return "editpassword"; // Trả về trang đổi mật khẩu
	}

	@PostMapping("/edit-password")
	public String editPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
	    IUsersDAO userDao = new ImplUsersDao();
	    Users user = userDao.findByEmail(email); // Tìm người dùng theo email
	    if (user == null) {
	        redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại!");
	        return "redirect:/edit-password";
	    }
	    
	    // Mã hóa mật khẩu mới
	    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
	    user.setPassword(hashedPassword);
	    user.setOtp(null); // Xóa OTP sau khi đổi mật khẩu thành công
	    user.setOtpCreatedAt(null);
	    user.setCreatedAt(new Date());
	    userDao.update(user); // Cập nhật người dùng với mật khẩu mới
	    
	    redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công!");
	    return "redirect:/index"; // Chuyển hướng về trang đăng nhập
	}
	
}
