<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
 <div class="container mt-5">
          <div class="row">
            <!-- Form đăng nhập nằm ở 1/3 bên phải -->
            <div class="col-4 ms-auto">
				<form action="${rootpath}edit-password" method="post">
				    <input type="hidden" name="email" value="${email}">
				    <div class="mb-3">
				        <label for="newPassword">Mật khẩu mới:</label>
				        <input type="password" class="form-control" id="newPassword" name="newPassword" required>
				    </div>
				    <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
				</form>
			</div>
		   </div>
	</div>


</body>
</html>