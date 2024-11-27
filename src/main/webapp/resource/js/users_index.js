function previewImages(event) {
    var input = event.target;
    var previewContainer = document.getElementById('imagePreviewContainer');
    previewContainer.innerHTML = ""; // Xóa nội dung cũ trước khi thêm ảnh mới

    if (input.files) {
        Array.from(input.files).forEach(function(file) {
            if (file.type.startsWith("image/")) { // Kiểm tra file có phải ảnh không
                var reader = new FileReader();

                reader.onload = function(e) {
                    // Tạo thẻ img mới để hiển thị ảnh
                    var img = document.createElement('img');
                    img.src = e.target.result;
                    img.style.maxWidth = "150px";
                    img.style.maxHeight = "150px";
                    img.style.border = "1px solid #ccc";
                    img.style.padding = "5px";

                    // Thêm ảnh vào container
                    previewContainer.appendChild(img);
                };

                reader.readAsDataURL(file);
            }
        });
    }
}
