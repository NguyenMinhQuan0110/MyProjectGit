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
<link rel="stylesheet" href="${rootpath}resource/css/style.css">
</head>
<body>
<!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty message}">
            <div class="alert alert-danger mt-3">${message}</div>
        </c:if>
<div class="container mt-5">
          <div class="row">
            <!-- Form đăng nhập nằm ở 1/3 bên phải -->
            <div class="col-4 ms-auto">
                <h2>Nhập Mã OTP</h2>

                <!-- Form đăng nhập -->
                <form action="${rootpath}checkotp" method="post">
                    <div class="mb-3">
                        <label for="otp" class="form-label">Mã OTP</label>
                        <input type="text" class="form-control" id="otp" name="otp" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Xác nhận</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>