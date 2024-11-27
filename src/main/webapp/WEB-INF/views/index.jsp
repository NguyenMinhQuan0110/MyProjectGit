<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:url value="/" var="rootpath"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- Latest compiled and minified CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
 <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger mt-3">${errorMessage}</div>
        </c:if>
    <div class="container mt-5">
          <div class="row">
            <!-- Form đăng nhập nằm ở 1/3 bên phải -->
            <div class="col-4 ms-auto">
                <h2>Đăng nhập</h2>

                <!-- Form đăng nhập -->
                <form action="${rootpath}login" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <p>Bạn quyên mật khẩu ư ? - <a href="${rootpath}email">Yes</a></p>
                     <!-- Sử dụng flexbox để căn chỉnh hai nút cách xa nhau -->
                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">Đăng nhập</button>
                        
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>