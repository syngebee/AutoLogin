<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>查询所有联系人</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="/js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="/js/bootstrap.min.js"></script>
    <style type="text/css">
        tr, th {
            text-align: center;
        }

        h4 {
            color: green;
        }
    </style>
    <script>
        $(function () {
            $("#selectAll").click(function () {
                $("input[type=checkbox]").prop("checked", this.checked)
            });

            $(".dataCheckBox").click(function () {
                if ($(".dataCheckBox").length == $(".dataCheckBox:checked").length) {
                    $("#selectAll").prop("checked", true);
                } else {
                    $("#selectAll").prop("checked", false);
                }
            });

            $("#deleteAllBtn").click(function () {
                let $dataCheckBox = $(".dataCheckBox:checked");
                let len = $dataCheckBox.length;
                if (len == 0) {
                    alert("请勾选删除的数据");
                } else if (confirm("确认删除这" + len + "条数据?")) {
                    let param = "";
                    $.each($dataCheckBox, function (index, element) {
                        param += "id=" + $(element).val() + "&";
                    });
                    param = param.substring(0, param.length - 1);
                    location.href = "/contact?action=deleteAllServlet&" + param;
                }
            });
            $("#current").val("${page.current}");

            $("#searchBtn").click(function () {
                $("#current").val("1");
                // $("#contactForm").submit();
            });

            $("#logoutBtn").click(function () {
                location.href = "/logoutServlet";
            });

            $("#submitBtn").click(function () {
                $("#current").val("1");
                // $("#contactForm").submit();
            })
        });

        function deleteOne(obj) {
            $("#current").val("1");
            if (confirm("是否确定要删除此条数据?")) {
                location.href = "/contact?action=deleteServlet&id=" + obj;
            }
        }

        function skipTo() {
            let val = $("#current").val();
            $("#contactForm").submit();
        }


    </script>
</head>
<body>
<div class="container">
    <h4>联系人列表</h4>
    <div class="row text-right" style="margin-bottom: 10px; margin-top: 15px;">
        <h4>欢迎回来,${userStatus.username}</h4>
        <button class="btn btn-default" id="deleteAllBtn"><i class="glyphicon glyphicon-delete"></i>批量删除</button>
        <button class="btn btn-default" id="logoutBtn"><i class="glyphicon glyphicon-off"></i>退出登录</button>
    </div>

    <form action="/contact" method="get" class="form-inline" id="contactForm">
        <input type="hidden" name="action" value="findByPageServlet"/>

        <%--<a class="btn btn-info btn-xs" id="deleteAllBtn">批量删除</a>--%>
        <div class="row text-right" style="margin-bottom: 10px; margin-top: 15px;">
            姓名：<input type="text" name="name" class="form-control" placeholder="搜索的名字" style="width: 120px;" id="name"
                      value="${condition.name}"/>
            年龄：
            <input type="number" name="min" class="form-control" style="width: 60px;" id="min"
                   value="${condition.min}"/>~
            <input type="number" name="max" class="form-control" style="width: 60px;" id="max"
                   value="${condition.max}"/>
            <button class="btn btn-default" id="searchBtn"><i class="glyphicon glyphicon-search"></i>查询</button>
        </div>
        <div class="row">
            <table border="1" class="table table-bordered table-hover">
                <tr class="success">
                    <th><input type="checkbox" id="selectAll"></th>
                    <th>编号</th>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>年龄</th>
                    <th>籍贯</th>
                    <th>QQ</th>
                    <th>邮箱</th>
                    <th>操作</th>
                </tr>

                <c:forEach items="${page.showList}" var="contact" varStatus="s">
                    <tr>
                        <td><input type="checkbox" class="dataCheckBox" value="${contact.id}"></td>
                        <td>${s.count+(page.current-1)*page.size}</td>
                        <td>${contact.name}</td>
                        <td>${contact.sex}</td>
                        <td>${contact.age}</td>
                        <td>${contact.address}</td>
                        <td>${contact.qq}</td>
                        <td>${contact.email}</td>
                        <td>
                            <div class="btn-group btn-group-sm">
                                <a class="btn btn-success btn-xs"
                                   href="/contact?action=getContactServlet&id=${contact.id}&updatePage=${page.current}">修改</a>&nbsp;
                                <button class="btn btn-info btn-xs" onclick="deleteOne('${contact.id}')">删除</button>
                                    <%--<a class="btn btn-info btn-xs" href="/contact?action=deleteServlet&id=${contact.id}">删除</a>--%>
                            </div>
                            .
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="9">
                        <div class="btn-group btn-group-sm">
                            <a class="btn btn-primary btn-xs" href="/contact/add.jsp">添加</a>
                        </div>
                    </td>
                </tr>
            </table>
        </div>

        <div class="row text-center">
            <div class="btn-group btn-group-sm">
                <c:if test="${page.current!=1}">
                    <a href="/contact?action=findByPageServlet&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                       class="btn btn-default">首页</a>
                </c:if>
                <c:if test="${page.current==1}">
                    <a href="JavaScript:void(0)" class="btn btn-default">首页</a>
                </c:if>

                <c:if test="${page.current!=1}">
                    <a href="/contact?action=findByPageServlet&current=${page.current-1}&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                       class="btn btn-default">上页</a>
                </c:if>
                <c:if test="${page.current==1}">
                    <a href="JavaScript:void(0)" class="btn btn-default">上页</a>
                </c:if>

                <c:forEach begin="${page.current-4>0?page.current-4:1}"
                           end="${page.current+5>page.totalPage?page.totalPage:page.current+5}" step="1" var="index">
                    <c:if test="${index==page.current}">
                        <a href="/contact?action=findByPageServlet&current=${index}&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                           class="btn btn-default active">${index}</a>
                    </c:if>
                    <c:if test="${index!=page.current}">
                        <a href="/contact?action=findByPageServlet&current=${index}&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                           class="btn btn-default">${index}</a>
                    </c:if>
                </c:forEach>

                <c:if test="${page.current!=page.totalPage}">
                    <a href="/contact?action=findByPageServlet&current=${page.current+1}&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                       class="btn btn-default">下页</a>
                </c:if>
                <c:if test="${page.current==page.totalPage}">
                    <a href="JavaScript:void(0)" class="btn btn-default">下页</a>
                </c:if>

                <c:if test="${page.current!=page.totalPage}">
                    <a href="/contact?action=findByPageServlet&current=${page.totalPage}&name=${condition.name}&min=${condition.min}&max=${condition.max}"
                       class="btn btn-default">末页</a>
                </c:if>
                <c:if test="${page.current==page.totalPage}">
                    <a href="JavaScript:void(0)" class="btn btn-default">末页</a>
                </c:if>
            </div>
            每页
            <input type="number" class="form-control" name="size" value="${page.size}" style="width: 60px;" id="size"/>
            条
            <button id="submitBtn" class="btn btn-default">修改</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            第
            <select id="current" class="form-control" name="current" onchange="skipTo()">
                <c:forEach begin="1" end="${page.totalPage}" step="1" var="index">
                    <c:if test="${index==page.current}">
                        <option value="${index}" class="active">${index}</option>
                    </c:if>
                    <c:if test="${index!=page.current}">
                        <option value="${index}">${index}</option>
                    </c:if>
                </c:forEach>
            </select>
            页/共${page.totalPage}页
            共${page.totalCount}条
            <%--<input type="submit" class="btn btn-default" value="确定">--%>


        </div>
    </form>
</div>
<br>
<br>
</body>
<script type="text/javascript">
</script>

</html>
