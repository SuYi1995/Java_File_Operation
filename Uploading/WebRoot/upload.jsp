<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传</title>
</head>
<body>
    <form action="${pageContext.request.contextPath }/upload" enctype="multipart/form-data" method="post">
    <br/>
        上传文件: <input type="file" name="file1" multiple><br/>
        <input type="submit" value="提交">
    </form>
    <br/> <br/>
    <form action="${pageContext.request.contextPath }/start" enctype="multipart/form-data" method="post">
     <input type="submit" value="开启服务器">
    </form>
    <form action="${pageContext.request.contextPath }/close" enctype="multipart/form-data" method="post">
     <input type="submit" value="关闭服务器">
    </form>
</body>
</html>