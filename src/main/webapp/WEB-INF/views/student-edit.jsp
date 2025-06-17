<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改学生</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.ico" type="image/ico">
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet">
    <!-- 日期选择插件样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap-datepicker/bootstrap-datepicker3.min.css">
</head>

<body>
<div class="lyear-layout-web">
    <div class="lyear-layout-container">

        <!-- 引入侧边栏和头部导航 -->
        <jsp:include page="_aside_header.jsp"></jsp:include>

        <!-- 页面主要内容区域 -->
        <main class="lyear-layout-content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <!-- 学生信息修改表单，提交到student Servlet的edit操作 -->
                                <form id="myForm" action="${pageContext.request.contextPath}/student?r=edit" method="post">
                                    <!-- 学号字段（只读，不可修改） -->
                                    <div class="form-group">
                                        <label for="sno">学号</label>
                                        <input readonly value="${entity.sno}" class="form-control" type="text" name="sno" id="sno" aria-label="学号">
                                    </div>
                                    <!-- 密码字段（必填） -->
                                    <div class="form-group">
                                        <label for="password">密码</label>
                                        <input required value="${entity.password}" class="form-control" type="password" name="password" id="password" aria-label="密码" autocomplete="new-password">
                                    </div>
                                    <!-- 姓名字段（必填） -->
                                    <div class="form-group">
                                        <label for="name">姓名</label>
                                        <input required value="${entity.name}" class="form-control" type="text" name="name" id="name" aria-label="姓名">
                                    </div>
                                    <!-- 电话字段（正则验证11位数字） -->
                                    <div class="form-group">
                                        <label for="tele">电话</label>
                                        <input class="form-control" value="${entity.tele}" type="tel" name="tele" id="tele" aria-label="电话" pattern="[0-9]{11}">
                                    </div>
                                    <!-- 入学日期字段（使用日期选择插件） -->
                                    <div class="form-group">
                                        <label for="enterdate">入学日期</label>
                                        <input value="${entity.enterdate}" class="form-control js-datepicker m-b-10" type="text"
                                               name="enterdate" id="enterdate" placeholder="yyyy-mm-dd" data-date-format="yyyy-mm-dd" aria-label="入学日期" />
                                    </div>
                                    <!-- 年龄字段（数字范围1-100） -->
                                    <div class="form-group">
                                        <label for="age">年龄</label>
                                        <input value="${entity.age}" class="form-control" type="number" name="age" id="age" aria-label="年龄" min="1" max="100">
                                    </div>
                                    <!-- 性别选择（使用JSTL动态选中当前值） -->
                                    <div class="form-group">
                                        <label for="gender">性别</label>
                                        <select class="form-control" name="gender" id="gender" size="1" aria-label="性别">
                                            <option value="">请选择</option>
                                            <option <c:if test="${entity.gender == 'm'}">selected</c:if> value="m">男</option>
                                            <option <c:if test="${entity.gender == 'w'}">selected</c:if> value="w">女</option>
                                        </select>
                                    </div>
                                    <!-- 地址字段 -->
                                    <div class="form-group">
                                        <label for="address">详细地址</label>
                                        <textarea class="form-control" name="address" id="address" aria-label="详细地址">${entity.address}</textarea>
                                    </div>
                                    <!-- 班级选择（使用JSTL循环生成选项并动态选中当前班级） -->
                                    <div class="form-group">
                                        <label for="clazzno">班级</label>
                                        <select class="form-control" name="clazzno" id="clazzno" size="1" aria-label="班级">
                                            <option value="">请选择</option>
                                            <c:forEach items="${clazzes}" var="i" varStatus="s">
                                                <option <c:if test="${entity.clazzno == i.clazzno}">selected</c:if> value="${i.clazzno}">${i.clazzno} / ${i.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- 提交按钮 -->
                                    <div class="form-group">
                                        <button class="btn btn-primary" type="submit">提交</button>
                                    </div>
                                </form>
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
        // 表单提交事件处理
        $('#myForm').on('submit',function (event) {
            event.preventDefault();
            // 显示加载动画
            lightyear.loading('show');
            // 序列化表单数据
            var formData = $(this).serialize();
            // 发送AJAX请求
            $.ajax({
                type: 'post',
                url: $(this).attr('action'),
                data: formData,
                success: function (response) {
                    // 处理响应结果
                    if (response.success) {
                        lightyear.loading('hide');
                        // 跳转到学生列表页面
                        lightyear.url('student');
                        // 显示成功提示
                        lightyear.notify(response.message, 'success', 500);
                    } else {
                        lightyear.loading('hide');
                        // 显示错误提示
                        lightyear.notify(response.message, 'danger', 3000);
                    }
                },
                error: function () {
                    lightyear.loading('hide');
                    lightyear.notify("请求失败，请检查！", 'danger', 3000);
                }
            });
        });
    });
</script>
</body>
</html>