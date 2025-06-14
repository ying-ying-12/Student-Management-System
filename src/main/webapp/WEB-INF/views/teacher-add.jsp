<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>新增教师</title>
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
                                <form id="myForm" action="${pageContext.request.contextPath}/teacher?r=add" method="post">
                                    <div class="form-group">
                                        <label >教师编号</label>
                                        <input required class="form-control" type="text" name="tno">
                                    </div>
                                    <div class="form-group">
                                        <label >密码</label>
                                        <input required class="form-control" type="password" name="password">
                                    </div>
                                    <div class="form-group">
                                        <label >姓名</label>
                                        <input required class="form-control" type="text" name="tname">
                                    </div>

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
            <%--提交之后发送ajax请求--%>
            $.ajax({
                type:'post',
                url:$(this).attr('action'),
                data:formData,
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

        })
    });
</script>
</body>
</html>
