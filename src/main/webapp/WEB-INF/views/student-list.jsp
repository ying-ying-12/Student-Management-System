<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>学生列表</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.ico" type="image/ico">
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet">
</head>

<body>
<div class="lyear-layout-web">
    <div class="lyear-layout-container">

        <!-- 引入侧边栏和头部导航 -->
        <jsp:include page="_aside_header.jsp"></jsp:include>

        <!-- 页面主要内容 -->
        <main class="lyear-layout-content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header"><h4>学生列表</h4></div>
                            <div class="card-body">
                                <!-- 搜索表单：支持多条件组合查询 -->
                                <form id="form" style="margin-bottom: 20px" class="form-inline" action="${pageContext.request.contextPath}/student" method="get">
                                    <input type="hidden" id="current" name="current" value="1"> <!-- 当前页码 -->

                                    <!-- 搜索条件：学号、姓名、性别、班级 -->
                                    <div class="form-group">
                                        <label>学号</label>
                                        <input class="form-control" type="text" value="${sno}" name="sno" placeholder="请输入学号..">
                                    </div>
                                    <div class="form-group">
                                        <label>姓名</label>
                                        <input class="form-control" type="text" value="${empty name ? '' : name}" name="name" placeholder="请输入姓名..">
                                    </div>
                                    <div class="form-group">
                                        <label>性别</label>
                                        <select class="form-control" name="gender" size="1">
                                            <option value="">请选择</option>
                                            <option <c:if test="${gender == 'm'}">selected</c:if> value="m">男</option>
                                            <option <c:if test="${gender == 'w'}">selected</c:if> value="w">女</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>班级编号</label>
                                        <input class="form-control" type="text" value="${clazzno}" name="clazzno" placeholder="请输入班级编号..">
                                    </div>

                                    <!-- 操作按钮：查询和新增（仅管理员可见） -->
                                    <div class="form-group">
                                        <button class="btn btn-brown btn-round" type="submit">查询</button>
                                        <c:if test="${sessionScope.role == 'admin'}">
                                            <button class="btn btn-brown btn-success" type="button" onclick="location.href='?r=add'">新增</button>
                                        </c:if>
                                    </div>
                                </form>

                                <!-- 学生数据表格 -->
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>学号</th>
                                        <th>姓名</th>
                                        <th>电话</th>
                                        <th>入学时间</th>
                                        <th>年龄</th>
                                        <th>性别</th>
                                        <th>详细地址</th>
                                        <th>班级</th>
                                        <c:if test="${sessionScope.role == 'admin'}">
                                            <th>操作</th>
                                        </c:if>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <!-- 循环遍历学生列表数据 -->
                                    <c:forEach items="${pagerVO.list}" var="i" varStatus="s">
                                        <tr>
                                            <th scope="row">${s.count}</th>
                                            <td>${i.sno}</td>
                                            <td>${i.name}</td>
                                            <td>${i.tele}</td>
                                            <td><fmt:formatDate value="${i.enterdate}" pattern="yyyy年MM月dd日" /></td>
                                            <td>${i.age}</td>
                                            <td>${i.gender == 'm'?'男':(i.gender == 'w'?'女':'')}</td>
                                            <td>${i.address}</td>
                                            <td>${i.clazz.clazzno} / ${i.clazz.name}</td>

                                            <!-- 操作列：编辑和删除（仅管理员可见） -->
                                            <c:if test="${sessionScope.role == 'admin'}">
                                                <td>
                                                    <button class="btn btn-primary btn-xs" type="button" onclick="location.href='?r=edit&sno=${i.sno}'">编辑</button>
                                                    <button class="btn btn-danger btn-xs" type="button" onclick="del('${i.sno}')">删除</button>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <!-- 引入分页导航组件 -->
                                <jsp:include page="_pager.jsp"></jsp:include>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>

<!-- 引入公共JS资源 -->
<jsp:include page="_js.jsp"></jsp:include>

<script type="text/javascript">
    $(document).ready(function (e) {
        // 页面初始化逻辑
    });

    /**
     * 跳转到指定页码
     * @param page 目标页码
     */
    function gotoPage(page) {
        $("#current").val(page);  // 设置当前页码
        $("#form").submit();      // 提交表单触发查询
    }

    /**
     * 删除学生信息
     * @param sno 要删除的学生学号
     */
    function del(sno) {
        lightyear.loading('show');  // 显示加载动画

        // 发送AJAX请求删除学生
        $.ajax({
            type: 'post',
            url: 'student?r=del',
            data: {sno},
            success: function (response) {
                if (response.success) {
                    lightyear.loading('hide');  // 隐藏加载动画
                    lightyear.url('student');   // 刷新页面
                    lightyear.notify(response.message, 'success', 500);  // 显示成功提示
                } else {
                    lightyear.loading('hide');  // 隐藏加载动画
                    lightyear.notify(response.message, 'danger', 3000);  // 显示错误提示
                }
            },
            error: function () {
                lightyear.loading('hide');  // 隐藏加载动画
                lightyear.notify("请求失败，请检查！", 'danger', 3000);  // 显示网络错误提示
            }
        });
    }
</script>
</body>
</html>