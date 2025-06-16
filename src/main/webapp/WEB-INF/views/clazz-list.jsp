<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>班级列表</title>
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
                        <div class="card-header"><h4>班级列表</h4></div>
                        <div class="card-body">
                            <form id="form" style="margin-bottom: 20px" class="form-inline" action="${pageContext.request.contextPath}/clazz" method="get">
                                <input type="hidden" id="current" name="current" value="1">
                                <div class="form-group">
                                    <label >班级编号</label>
                                    <input class="form-control" type="text" value="${clazzno}" name="clazzno" placeholder="请输入班级编号..">
                                </div>
                                <div class="form-group">
                                    <label>班级名</label>
                                    <input class="form-control" type="text" value="${empty name ? '' : name}" name="name" placeholder="请输入班级名..">
                                </div>
                                <div class="form-group">
                                    <button class="btn btn-brown btn-round" type="submit">查询</button>
<c:if test="${sessionScope.role == 'admin'}">
                                    <button class="btn btn-brown btn-success" type="button" onclick="location.href='?r=add'">新增</button>
</c:if>
                                </div>
                            </form>

                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>班级编号</th>
                                    <th>班级名称</th>
                                    <c:if test="${sessionScope.role == 'admin'}"><th>操作</th></c:if>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${pagerVO.list}" var="i" varStatus="s">
                                <tr>
                                    <th scope="row">${s.count}</th>
                                    <td>${i.clazzno}</td>
                                    <td>${i.name}</td>

                                    <c:if test="${sessionScope.role == 'admin'}">
                                    <td>
                                        <button class="btn btn-primary btn-xs" type="button" onclick="location.href='?r=edit&clazzno=${i.clazzno}'">编辑</button>
                                        <button class="btn btn-danger btn-xs" type="button" onclick="del('${i.clazzno}')">删除</button>
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
    function del(clazzno) {
        lightyear.loading('show');
        $.ajax({
            type:'post',
            url:'clazz?r=del',
            data:{clazzno},
            success:function (response) {
                if(response.success){
                    lightyear.loading('hide');
                    lightyear.url('clazz');
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
