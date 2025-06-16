<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>课程列表</title>
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
                            <div class="card-header"><h4>课程列表</h4></div>
                            <div class="card-body">
                                <form id="form" style="margin-bottom: 20px" class="form-inline" action="${pageContext.request.contextPath}/course" method="get">
                                    <input type="hidden" id="current" name="current" value="1">
                                    <div class="form-group">
                                        <label >课程编号</label>
                                        <input class="form-control" type="text" value="${cno}" name="cno">
                                    </div>
                                    <div class="form-group">
                                        <label>课程名</label>
                                        <input class="form-control" type="text" value="${cname}" name="cname" placeholder="">
                                    </div>
                                    <div class="form-group">
                                        <label>教师</label>
                                        <select class="form-control" name="tno" size="1">
                                            <option value="">请选择</option>
                                            <c:forEach items="${teacheres}" var="i" varStatus="s">
                                                <option <c:if test="${tno == i.tno}">selected</c:if> value="${i.tno}">${i.tno} / ${i.tname}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <button class="btn btn-brown btn-round" type="submit">查询</button>
                                        <c:if test="${sessionScope.role == 'teacher'}">
                                            <button class="btn btn-brown btn-success" type="button" onclick="location.href='?r=add'">新增</button>
                                        </c:if>
                                    </div>
                                </form>

                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>课程编号</th>
                                        <th>任课教师</th>
                                        <th>课程名</th>
                                        <th>开始选课时间</th>
                                        <th>选课结束时间</th>
                                        <th>学分</th>
                                        <th>限制人数</th>
                                        <th>已选人数</th>
                                        <c:if test="${sessionScope.role == 'teacher'}">
                                            <th>操作</th>
                                        </c:if>
                                        <c:if test="${sessionScope.role == 'student'}">
                                            <th>选课</th>
                                        </c:if>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pagerVO.list}" var="i" varStatus="s">
                                        <tr>
                                            <th scope="row">${s.count}</th>
                                            <td>${i.cno}</td>
                                            <td>${i.tno}/${i.teacher.tname}</td>
                                            <td>${i.cname}</td>
                                            <td><fmt:formatDate value="${i.begindate}" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
                                            <td><fmt:formatDate value="${i.enddate}" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
                                            <td>${i.credits}</td>
                                            <td>${i.limi}</td>
                                            <td>${i.count}</td>
                                            <c:if test="${sessionScope.role == 'student'}">
                                                <td>
                                                    <c:if test="${i.limi > i.count && i.begindate <= now && i.enddate > now}">
                                                        <button class="btn btn-danger btn-xs" type="button" onclick="choose('${i.cno}')">选课</button>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                            <c:if test="${sessionScope.role == 'teacher'}">
                                                <td>
                                                    <%--教师只能编辑和删除自己课程--%>
                                                    <c:if test="${sessionScope.user.tno == i.tno}">
                                                    <button class="btn btn-primary btn-xs" type="button" onclick="location.href='?r=edit&cno=${i.cno}'">编辑</button>
                                                    <button class="btn btn-danger btn-xs" type="button" onclick="del('${i.cno}')">删除</button>
                                                    </c:if>
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
    function del(cno) {
        lightyear.loading('show');
        $.ajax({
            type:'post',
            url:'course?r=del',
            data:{cno},
            success:function (response) {
                if(response.success){
                    lightyear.loading('hide');
                    lightyear.url('course');
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
    function choose(cno) {
        if (confirm("确认选课？")) {
            lightyear.loading('show');
            $.ajax({
                type: 'post',
                url: '${pageContext.request.contextPath}/stucou?r=add',
                data: {cno},
                success: function (response) {
                    if (response.success) {
                        lightyear.loading('hide');
                        lightyear.url('${pageContext.request.contextPath}/stucou');
                        lightyear.notify(response.message, 'success', 500);
                    } else {
                        lightyear.loading('hide');
                        lightyear.notify(response.message, 'danger', 3000);
                    }
                },
                error: function () {
                    lightyear.loading('hide');
                    lightyear.notify("请求失败，请检查！", 'danger', 3000);
                }
            })
        }
    }
</script>
</body>
</html>

