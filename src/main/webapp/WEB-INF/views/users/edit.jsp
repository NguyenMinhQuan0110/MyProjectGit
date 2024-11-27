<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:url value="/" var="rootpath"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit User</title>
<!-- Latest compiled and minified CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${rootpath}resource/css/style.css">
</head>
<body>
	<div class="container mt-5">
		<div class="header">
	            <h3>${sessionScope.loggedInUser.username}!</h3>
	            <!-- Nút đăng xuất -->
	            <form action="${rootpath}logout" method="get" style="margin-left: 20px;">
	                <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');">Đăng xuất</button>
	            </form>
	        </div>
        <h2>Sửa Thông Tin Người Dùng</h2>
        <form action="${rootpath}users/edit" method="post">
            <input type="hidden" name="userID" value="${user.userID}">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username" value="${user.username}" required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password">
                <small class="form-text text-muted">Để trống nếu không muốn thay đổi mật khẩu.</small>
            </div>
            <button type="submit" class="btn btn-primary" onclick="return confirm('Bạn có chắc chắn muốn sửa người dùng này?');">Cập Nhật</button>
        </form>
    </div>
</body>
</html>