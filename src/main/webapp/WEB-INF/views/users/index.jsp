<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:url value="/" var="rootpath"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List Users</title>
<!-- Latest compiled and minified CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${rootpath}resource/css/style.css">
<script src="${rootpath}resource/js/users_index.js"></script>
</head>
<body>
	<!-- Hiển thị alert nếu có message -->
	<c:if test="${not empty message}">
	    <c:choose>
	        <c:when test="${messageType == 'success'}"> 
	            <div class="alert alert-success" role="alert" id="alertMessage"> 
	                ${message} 
	            </div> 
	        </c:when> 
	        <c:when test="${messageType == 'danger'}">
	            <div class="alert alert-danger" role="alert" id="alertMessage">
	                ${message}
	            </div>
	        </c:when>
	    </c:choose>
	</c:if>

	<script>
	    // Kiểm tra xem có thông báo không và ẩn nó sau 3 giây
	    window.onload = function() {
	        var alertMessage = document.getElementById("alertMessage");
	        if (alertMessage) {
	            setTimeout(function() {
	                alertMessage.style.display = "none";
	            }, 2000); // 3000 milliseconds = 3 seconds
	        }
	    };
	</script>
	<div class="container mt-5">
			<div class="header">
	            <h3>${sessionScope.loggedInUser.username}!</h3>
	            <!-- Nút đăng xuất -->
	            <form action="${rootpath}logout" method="get" style="margin-left: 20px;">
	                <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');">Đăng xuất</button>
	            </form>
	        </div>
	        <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-whatever="@mdo">Tạo bài đăng</button>
	         <button type="button" class="btn btn-outline-success"><a href="${rootpath}posts/postsofuser" style="text-decoration: none;">Các bài đăng</a></button>		
			<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">New message</h5>
			        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			      </div>
			      <div class="modal-body">
			        <form action="${rootpath}users/creatpost" method="post" enctype="multipart/form-data">
			        	
			          <div class="mb-3">
			            <label for="message-text" class="col-form-label">Content:</label>
			            <textarea class="form-control" id="message-text" name="content"></textarea>
			          </div>
					   <div class="mb-3">
					       <label for="fileAnh" class="col-form-label">Upload Image:</label>
					       <input type="file" class="form-control" id="fileAnh" name="fileAnh" accept="image/*" multiple onchange="previewImages(event)">
							<div id="imagePreviewContainer" style="display: flex; flex-wrap: wrap; gap: 10px;"></div>
					   </div>
			           <div class="modal-footer">
					        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
					        <button type="submit" class="btn btn-primary">Create</button>
					   </div>
			        </form>
			      </div>
			     
			    </div>
			  </div>
			</div>
			
			<h2>From thêm users</h2>
			<form action="${rootpath}users/add" method="post">
					    <div class="mb-3 col-6">
					        <label for="exampleInputUsername" class="form-label">Username</label>
					        <input type="text" class="form-control" id="exampleInputUsername" name="username" required>
					    </div>
					    <div class="mb-3 col-6">
					        <label for="exampleInputEmail1" class="form-label">Email address</label>
					        <input type="email" class="form-control" id="exampleInputEmail1" name="email" required>
					    </div>
					    <div class="mb-3 col-6">
					        <label for="exampleInputPassword1" class="form-label">Password</label>
					        <input type="password" class="form-control" id="exampleInputPassword1" name="password" required>
					    </div>
					    <button type="submit" class="btn btn-primary">Submit</button>
			</form>
	</div>
	<div class="container mt-3">
	    <form action="${rootpath}users/search" method="get">
	        <div class="input-group mb-3 col-6">
	            <input type="text" class="form-control" name="keyword" placeholder="Tìm kiếm người dùng" aria-label="Search">
	            <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
	        </div>
	    </form>
	</div>
	<div class="container mt-3">
		  <h2>Users Table</h2>           
		  <table class="table table-hover">
		    <thead>
		      <tr>
		        <th>Username</th>
		        <th>Email</th>
		        <th>CreatedAt</th>
		        <th></th>
		        <th></th>
		      </tr>
		    </thead>
		    <tbody>
			    <c:forEach items="${lst}" var="item">
				   <tr>
				        <td>${item.username}</td>
				        <td>${item.email}</td>
				        <td><fmt:formatDate value="${item.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
				        <td>
		                <form action="${rootpath}users/initedit" method="get" style="display:inline;">
						    <input type="hidden" name="userId" value="${item.userID}">
						    <button type="submit" class="btn btn-warning">Sửa</button>
						</form>
		                <form action="${rootpath}/users/delete" method="post" style="display:inline;">
		                    <input type="hidden" name="userId" value="${item.userID}">
		                    <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng: ${item.username} ?');">Xóa</button>
		                </form>
            			</td>
			      </tr>
				</c:forEach>
		    </tbody>
		  </table>
	</div>
</body>
</html>