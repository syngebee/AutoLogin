<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>登录页面</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="/css/login.css" rel="stylesheet">
    <script src="/js/jquery-2.1.0.min.js"></script>
    <script>
        //点击更换验证码图片
        function changeImg(img) {
            //html中元素的属性发生改变的时候,浏览器自动刷新
            var time = new Date().getTime()
            img.src = "/code?time=" + time
        }

    </script>

</head>

<body>

<div class="container">
    <%-- TODO:设置登录的路径 --%>
    <form class="form-signin" action="/loginServlet" method="POST" id="loginForm">
        <div class="text-center">
            <h2 class="form-signin-heading">登录页面</h2>
            <span style="color: red">
                <%-- 展示错误的提示信息 --%>
                ${msg}
            </span>
        </div>
        <%--  用户名  --%>
        <label for="inputEmail" class="sr-only">用户名</label>
        <input type="text" id="inputEmail" name="username"
               value="" class="form-control" placeholder="用户名" required autofocus><span>tom</span>
        <%--  密码  --%>
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" id="inputPassword" name="password" value=""
               class="form-control" placeholder="密码" required><span>123</span>
        <%-- 验证码 --%>
        <input type="text" id="checkcode" name="ucode" class="form-control" placeholder="验证码"><span>后门itheima</span>
        <img src="/code" onclick="changeImg(this)">

        <div class="checkbox">
            <label>
                <input type="checkbox" id="cb" name="remember" value="yes"> 记住账号密码(7天免登录)
            </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" id="loginBtn">登录</button>
        <%--<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>--%>
    </form>

</div>

</body>
</html>

