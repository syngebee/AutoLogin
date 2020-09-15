<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改用户</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/jquery-2.1.0.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#address").val("${contact.address}");
            if ("${contact.sex}" === "男") {
                $("#radioFeMale").prop("checked", false);
                $("#radioMale").prop("checked", true);
            }

            if ("${contact.sex}" === "女") {
                $("#radioFeMale").prop("checked", true);
                $("#radioMale").prop("checked", false);
            }
        })
    </script>
</head>
<body>
<div class="container" style="width: 400px;">
    <h3 style="text-align: center;">修改联系人 ${userStatus.username}</h3>
    <form action="/contact" method="post">
        <%--
        TODO:  input type=hidden 用户不可见的
            id用户不需要知道,后端又需要
            注意: 放在form标签内部
        --%>
        <input type="hidden" name="action" value="updateServlet">
        <input type="hidden" name="id" value="${contact.id}">
        <input type="hidden" name="updatePage" value="${updatePage}"/>
        <div class="form-group">
            <label for="name">姓名：</label>
            <input type="text" class="form-control" id="name" name="name" value="${contact.name}" readonly="readonly"
                   placeholder="请输入姓名"/>
        </div>

        <div class="form-group">
            <label>性别：</label>
            <input type="radio" name="sex" value="男" id="radioMale"/>男
            <input type="radio" name="sex" value="女" id="radioFeMale"/>女
        </div>

        <div class="form-group">
            <label for="age">年龄：</label>
            <input type="text" class="form-control" id="age" name="age" placeholder="请输入年龄" value="${contact.age}"/>
        </div>

        <div class="form-group">
            <label for="address">籍贯：</label>
            <select name="address" class="form-control" id="address">
                <c:forEach items="${cityList}" var="city">
                    <option value="${city}">${city}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="qq">QQ：</label>
            <input type="text" class="form-control" name="qq" id="qq" placeholder="请输入QQ号码" value="${contact.qq}"/>
        </div>

        <div class="form-group">
            <label for="email">Email：</label>
            <input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱地址"
                   value="${contact.email}"/>
        </div>

        <div class="form-group" style="text-align: center">
            <input class="btn btn-primary" type="submit" value="提交"/>
            <input class="btn btn-default" type="reset" value="重置"/>
            <input class="btn btn-default" type="button" value="返回" onclick="history.back()"/>
        </div>
    </form>
</div>
</body>
</html>