<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="/" var="rootpath" />
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Posts</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="${rootpath}resource/css/style.css">
<script src="${rootpath}resource/js/users_index.js"></script>

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



<script>
	function toggleMenu(button) {
		// Tìm dropdown-menu gần nhất và thay đổi hiển thị
		const menu = button.nextElementSibling;
		if (menu.style.display === "none" || menu.style.display === "") {
			menu.style.display = "block"; // Hiển thị menu
		} else {
			menu.style.display = "none"; // Ẩn menu
		}
	}

	// Ẩn menu nếu click ra ngoài
	document.addEventListener('click', function(event) {
		const isClickInsideMenu = event.target.closest('.dropdown-menu, .btn-outline-secondary');
		if (!isClickInsideMenu) {
			// Ẩn tất cả các menu đang hiển thị
			document.querySelectorAll('.dropdown-menu').forEach(function(menu) {
				menu.style.display = 'none';
			});
		}
	});
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
	            }, 5000); // 3000 milliseconds = 3 seconds
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
		<a href="${rootpath}users"><button type="button"
				class="btn btn-primary" style="border-radius: 100px;height: 80px">HAHA</button></a>
	</div>
	<div class="container mt-3">
		<form action="${rootpath}posts/search" method="get">
			<div class="input-group mb-3 col-6"
				style="width: 50%; margin: 0 auto;">
				<input type="text" class="form-control" name="keyword"
					placeholder="" aria-label="Search">
				<button class="btn btn-outline-secondary" type="submit">Tìm
					kiếm</button>
			</div>
		</form>
	</div>
	<div class="container mt-3">
			<div class="input-group mb-3 col-6"
				style="width: 50%; margin: 0 auto;">
			<button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-whatever="@mdo">Tạo bài đăng của bạn</button>		
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
			</div>
	</div>
	<div class="container mt-3">
		<c:forEach items="${lst}" var="item">

			<div style="width: 50%; margin: 0 auto;">
				<hr>
				<div style="display: flex; flex-direction: row-reverse; justify-content: space-between;">
					<!-- Button chứa biểu tượng ba chấm -->
					<div style="position: relative;">
						<button type="button" class="btn btn-outline-secondary" style="height: 30px; padding: 1px" onclick="toggleMenu(this)">
							<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
								<path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"></path>
							</svg>
						</button>
			
						<!-- Menu hiện ra khi nhấn vào biểu tượng -->
						<div class="dropdown-menu" style="display: none; position: absolute; right: 0; top: 30px; background-color: white; border: 1px solid #ccc; z-index: 1000;">
							<!-- Nút mở modal sửa bài đăng -->
							<button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#editModal${item.postID}" data-bs-whatever="@mdo">Sửa tiêu đề</button>

							<!-- Form xóa bài đăng -->
							<form action="${rootpath}/posts/delete" method="post" style="display:inline;">
			                        <input type="hidden" name="postID" value="${item.postID}">
			                        <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa bài đăng: ${item.postID} ?');">Xóa</button>
			                </form> 		
						</div>
					</div>
			
					<!-- Nội dung bài đăng -->
					<div>
						<a style="text-decoration: none; color: black;" onmouseover="this.style.textDecoration='underline';" onmouseout="this.style.textDecoration='none';" 
						href="${rootpath}posts/profile/${item.user.userID}">${item.user.username}</a><br>
						<span style="font-size: 0.75em"><fmt:formatDate value="${item.createdAt}" pattern="yyyy-MM-dd HH:mm" /><br></span>
						<span style="font-family: sans-serif;">${item.content}</span>
					</div>
				</div>
				<!-- Hiển thị hình ảnh của bài đăng -->
				<div style="overflow-x: auto; white-space: nowrap; margin-top: 10px;">
				    <c:forEach items="${item.listImage}" var="image">
				        <img src="${rootpath}${image.imageAd}" alt="Image" style="display: inline-block; max-height: 150px; margin-right: 10px;">
				    </c:forEach>
				</div>
				<!--bình luận ,like bài viết  -->
				<div style="margin-top: 5px">
				    <div style="display: flex; justify-content: space-between; align-items: center;">
				        <div>
				             <span id="likeCount${item.postID}">Lượt Thích: ${likeCountsMap[item.postID]}</span>
				        </div>
				         <!-- Nút Bình luận -->
				        <div style="flex: 1; text-align: center;">
				        	    <a href="${rootpath}posts/${item.postID}">
				        	    <button type="submit" class="btn btn-outline-secondary">Bình luận(${commentCountsMap[item.postID]})</button>
				        	    </a>	 
				        </div>
				        <!-- Lấy trạng thái Like từ postLikesMap -->
			            <form action="javascript:void(0);" style="display: inline;" onsubmit="toggleLike(${item.postID})">
						    <button type="submit" class="btn ${postLikesMap[item.postID] ? 'btn-primary' : 'btn-light'}" id="likeButton${item.postID}">
						        ${postLikesMap[item.postID] ? 'Hủy Thích' : 'Thích'}
						    </button>
						</form>
				    </div>
				</div>
				
				<!-- Modal Sửa Bài Đăng -->
			    <div class="modal fade" id="editModal${item.postID}" tabindex="-1" aria-labelledby="editModalLabel${item.postID}" aria-hidden="true">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <h5 class="modal-title" id="editModalLabel${item.postID}">Sửa tiêu đề của bài đăng của ${item.user.username}</h5>
				        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				      </div>
				      <div class="modal-body">
				        <form action="${rootpath}posts/edit" method="post">
				        	<input type="hidden" name="postID" value="${item.postID}">
				          <div class="mb-3">
				            <label for="message-text" class="col-form-label">Content:</label>
				            <textarea class="form-control" id="message-text" name="content">${item.content}</textarea>
				          </div>
				           <div class="modal-footer">
						        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
						        <button type="submit" class="btn btn-primary">Cập nhật</button>
						   </div>
				        </form>
				      </div>				     
				    </div>
				  </div>
				</div>
			</div>
			
		</c:forEach>
	</div>
</body>
</html>
