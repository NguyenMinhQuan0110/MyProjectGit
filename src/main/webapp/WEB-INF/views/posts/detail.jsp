<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<c:url value="/" var="rootpath" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chi tiết bài đăng</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${rootpath}resource/css/style.css">
<!-- js cập nhật trạng thái like -->
<script>
    function toggleLike(postID) {
        // Gửi yêu cầu AJAX đến server
        var form = new FormData();
        form.append("postID", postID);

        // Tạo đối tượng XMLHttpRequest
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "${rootpath}likeport/toggle", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                // Khi yêu cầu thành công, cập nhật lại nút "Like" và số lượt like
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    var likeButton = document.getElementById('likeButton' + postID);
                    likeButton.innerText = response.liked ? "Hủy Thích" : "Thích";
                    likeButton.classList.toggle("btn-primary", response.liked);
                    likeButton.classList.toggle("btn-light", !response.liked);

                    // Cập nhật số lượt like
                    var likeCountSpan = document.getElementById('likeCount' + postID);
                    likeCountSpan.innerText = "Lượt thích: " + response.likeCount;
                }
            }
        };
        xhr.send(form);
    }
</script>
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
			<a  style="text-decoration: none; color: black;" onmouseover="this.style.textDecoration='underline';" onmouseout="this.style.textDecoration='none';" 
			href="${rootpath}posts/profile/${sessionScope.loggedInUser.userID}"><h3>${sessionScope.loggedInUser.username}!</h3></a>
			<!-- Nút đăng xuất -->
			<form action="${rootpath}logout" method="get"
				style="margin-left: 20px;">
				<button type="submit" class="btn btn-danger"
					onclick="return confirm('Bạn có chắc chắn muốn đăng xuất?');">Đăng
					xuất</button>
			</form>
		</div>
		<a href="${rootpath}posts/postsofuser"><button type="button"
				class="btn btn-primary" style="border-radius: 100px;height: 80px">HAHA</button></a>
	</div>
	<div class="container mt-3">
			<div style="width: 50%; margin: 0 auto;">
				<hr>
				<div style="display: flex; flex-direction: row-reverse; justify-content: space-between;">
					<!-- Button chứa biểu tượng ba chấm -->
					<div style="position: relative;">
						
					</div>
					<!-- Nội dung bài đăng -->
					<div>
						<a style="text-decoration: none; color: black;" onmouseover="this.style.textDecoration='underline';" onmouseout="this.style.textDecoration='none';" 
						href="${rootpath}posts/profile/${post.user.userID}">${post.user.username}</a><br>
						<span style="font-size: 0.75em"><fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm" /><br></span>
						<span style="font-family: sans-serif;">${post.content}</span>
					</div>
				</div>
				<!-- Hiển thị hình ảnh của bài đăng -->
				<div style="overflow-x: auto; white-space: nowrap; margin-top: 10px;">
				    <c:forEach items="${post.listImage}" var="image">
				        <img src="${rootpath}${image.imageAd}" alt="Image" style="display: inline-block; max-height: 150px; margin-right: 10px;">
				    </c:forEach>
				</div>
				<!--like bài viết  -->
				<div style="margin-top: 5px">
				    <div style="display: flex; justify-content: space-between; align-items: center;">
				        <div>
				             <span id="likeCount${post.postID}">Lượt Thích: ${likeCount} </span> <%--  Hiển thị số lượt thích --%>
				        </div>
		
				        <!-- Lấy trạng thái Like từ postLikesMap -->
			            <form action="javascript:void(0);" style="display: inline;" onsubmit="toggleLike(${post.postID})">
						    <button type="submit" class="btn ${liked ? 'btn-primary' : 'btn-light'}" id="likeButton${post.postID}">
						        ${liked ? 'Đã Thích' : 'Thích'}
						    </button>
						</form>
				    </div>
				</div>
				<!-- Phần viết bình luận -->
				<div style="margin-top: 20px;">
				    <form action="${rootpath}posts/addbl" method="post" style="display: flex; align-items: center;">
				        <input type="hidden" name="postID" value="${post.postID}">
				        <div class="form-group" style="flex: 1; margin-right: 10px;">
				            <label for="commentContent" style="display: none;">Viết bình luận:</label>
				            <textarea id="commentContent" name="content" class="form-control" rows="3" required style="width: 100%;"></textarea>
				        </div>
				        <button type="submit" class="btn btn-success">Gửi bình luận</button>
				    </form>
				</div>
				<!-- Danh sách bình luận -->
				<div style="margin-top: 20px;">
				    <h5>Bình luận(${commentCount}):</h5>
				    <c:forEach items="${lst}" var="item">
				        <div class="card mb-3">
						    <div class="card-body">
							        <div style="display: flex; justify-content: space-between; align-items: center;">
							            <!-- Tên người dùng -->
							            <h6 class="card-subtitle text-muted">${item.user.username}</h6>
							            <!-- Form xóa bài đăng -->
							            <form action="${rootpath}posts/deleteBL" method="post" style="margin: 0;">
							                <input type="hidden" name="commentID" value="${item.commentID}">
							                <input type="hidden" name="postID" value="${post.postID}">
							                <button type="submit" class="btn btn-outline-dark"
							                    onclick="return confirm('Bạn có chắc chắn muốn xóa bình luận này:  ${item.commentID}?');">Xóa</button>
							            </form>
							        </div>
							        <p class="card-text">${item.content}</p>
							        <p class="card-text">
							            <small class="text-muted">
							                <fmt:formatDate value="${item.createdAt}" pattern="yyyy-MM-dd HH:mm" />
							            </small>
							        </p>
							</div>
						</div>
				    </c:forEach>
				</div>
			</div>
	</div>
</body>
</html>