<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>新增学生</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.ico" type="image/ico">
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet">
    <!--日期选择插件-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap-datepicker/bootstrap-datepicker3.min.css">
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
                            <div class="card-body">
                                <form id="myForm" action="${pageContext.request.contextPath}/student?r=add" method="post">
                                    <div class="form-group">
                                        <label for="sno">学号</label>
                                        <input required class="form-control" type="text" name="sno" id="sno" aria-label="学号" placeholder="请输入学号">
                                    </div>
                                    <div class="form-group">
                                        <label for="password">密码</label>
                                        <input required class="form-control" type="password" name="password" id="password" aria-label="密码" placeholder="请输入密码" autocomplete="new-password">
                                    </div>
                                    <div class="form-group">
                                        <label for="name">姓名</label>
                                        <input required class="form-control" type="text" name="name" id="name" aria-label="姓名" placeholder="请输入姓名">
                                    </div>
                                    <div class="form-group">
                                        <label for="tele">电话</label>
                                        <input class="form-control" type="tel" name="tele" id="tele" aria-label="电话" placeholder="请输入电话号码" pattern="[0-9]{11}">
                                    </div>
                                    <div class="form-group">
                                        <label for="enterdate">入学日期</label>
                                        <input class="form-control js-datepicker m-b-10" type="text"
                                               name="enterdate" id="enterdate" placeholder="yyyy-mm-dd" value="" data-date-format="yyyy-mm-dd" aria-label="入学日期" />
                                    </div>
                                    <div class="form-group">
                                        <label for="age">年龄</label>
                                        <input class="form-control" type="number" name="age" id="age" aria-label="年龄" min="1" max="100">
                                    </div>
                                    <div class="form-group">
                                        <label for="gender">性别</label>
                                        <select class="form-control" name="gender" id="gender" size="1" aria-label="性别">
                                            <option value="">请选择</option>
                                            <option value="m">男</option>
                                            <option value="w">女</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="address">详细地址</label>
                                        <textarea class="form-control" name="address" id="address" aria-label="详细地址" placeholder="请输入详细地址"></textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="clazzno">班级</label>
                                        <select class="form-control" name="clazzno" id="clazzno" size="1" aria-label="班级">
                                            <option value="">请选择</option>
                                            <c:forEach items="${clazzes}" var="i">
                                            <option value="${i.clazzno}">${i.clazzno} / ${i.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <button class="btn btn-primary" type="submit" aria-label="提交学生信息">提交</button>
                                    </div>
                                </form>

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
        $('#myForm').on('submit',function (event) {
            event.preventDefault();
            lightyear.loading('show');
            var formData = $(this).serialize();
            $.ajax({
                type:'post',
                url:$(this).attr('action'),
                data:formData,
                success:function (response) {
                    if(response.success){
                        lightyear.loading('hide');
                        lightyear.url('student');
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

        })
    });
</script>
</body>
</html>
