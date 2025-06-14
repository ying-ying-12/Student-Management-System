<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>教师列表</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.ico" type="image/ico">
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet">
</head>

<body>
<div class="lyear-layout-web">
    <div class="lyear-layout-container">

        <jsp:include page="_aside_header.jsp"></jsp:include>

        <!--页面主要内容-->
        <main class="lyear-layout-content">

            <div class="container-fluid">

                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header"><h4>教师列表</h4></div>
                            <div class="card-body">
                                <form id="form" style="margin-bottom: 20px" class="form-inline" action="${pageContext.request.contextPath}/teacher" method="get">
                                    <input type="hidden" id="current" name="current" value="1">
                                    <%--查询条件--%>
                                    <div class="form-group">
                                        <label >教师编号</label>
                                        <input class="form-control" type="text" value="${tno}" name="tno">
                                    </div>
                                    <div class="form-group">
                                        <label>姓名</label>
                                        <input class="form-control" type="text" value="${tname}" name="tname" placeholder="">
                                    </div>

                                    <div class="form-group">
                                        <button class="btn btn-brown btn-round" type="submit">查询</button>
                                        <%--只有管理员有新增权限--%>
                                        <c:if test="${sessionScope.role == 'admin'}">
                                            <button class="btn btn-brown btn-success" type="button" onclick="location.href='?r=add'">新增</button>
                                        </c:if>
                                    </div>
                                </form>

                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>教师编号</th>
                                        <th>姓名</th>
                                        <c:if test="${sessionScope.role == 'admin'}">
                                            <th>操作</th>
                                        </c:if>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pagerVO.list}" var="i" varStatus="s">
                                        <tr>
                                            <th scope="row">${s.count}</th>
                                            <td>${i.tno}</td>
                                            <td>${i.tname}</td>
                                            <c:if test="${sessionScope.role == 'admin'}">
                                                <td>
                                                    <button class="btn btn-primary btn-xs" type="button" onclick="location.href='?r=edit&tno=${i.tno}'">编辑</button>
                                                    <button class="btn btn-danger btn-xs" type="button" onclick="del('${i.tno}')">删除</button>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <jsp:include page="_pager.jsp"></jsp:include>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <!--End 页面主要内容-->
    </div>
</div>

<jsp:include page="_js.jsp"></jsp:include>

<script type="text/javascript">
    $(document).ready(function (e) {

    });
    function gotoPage(page) {
        $("#current").val(page)
        $("#form").submit()
    }
    function del(tno) {
        lightyear.loading('show');
        $.ajax({
            type:'post',
            url:'teacher?r=del',
            data:{tno},
            success:function (response) {
                if(response.success){
                    lightyear.loading('hide');
                    lightyear.url('teacher');
                    lightyear.notify(response.message, 'success', 500);
                }else{
                    lightyear.loading('hide');
                    lightyear.notify(response.message, 'danger', 3000);
                }
            },
            error:function () {
                lightyear.loading('hide');
                lightyear.notify("请求失败，请检查！", 'danger', 3000);
            }
        })
    }
</script>
</body>
</html>
