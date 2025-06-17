<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>编辑课程</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.ico" type="image/ico">
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.min.css" rel="stylesheet">
    <!--日期选择插件-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap-datepicker/bootstrap-datepicker3.min.css">
    <!--时间选择插件-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css">
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
                                <form id="myForm" action="${pageContext.request.contextPath}/course?r=edit" method="post">
                                    <div class="form-group">
                                        <%--课程编号不能改--%>
                                        <label >课程编号</label>
                                        <input  readonly value="${entity.cno}" required class="form-control" type="text" name="cno">
                                    </div>
                                    <div class="form-group">
                                        <label >课程名</label>
                                        <input required value="${entity.cname}" class="form-control" type="text" name="cname">
                                    </div>
                                    <div class="form-group">
                                        <label >选课开始时间</label>
                                        <input value="${entity.begindate}" class="form-control js-datetimepicker" type="text"
                                               name="begindate" placeholder="请选择具体时间" data-side-by-side="true" data-locale="zh-cn" date-format="yyyy-MM-DD HH:mm:ss" />

                                    </div>
                                    <div class="form-group">
                                        <label >选课结束时间</label>
                                        <input value="${entity.enddate}" class="form-control js-datetimepicker" type="text"
                                               name="enddate" placeholder="请选择具体时间" data-side-by-side="true" data-locale="zh-cn" date-format="yyyy-MM-DD HH:mm:ss" />
                                    </div>
                                    <div class="form-group">
                                        <label >学分</label>
                                        <input value="${entity.credits}" class="form-control" type="number" name="credits">
                                    </div>
                                    <div class="form-group">
                                        <%--限制人数不可编辑--%>
                                        <label >限制人数</label>
                                        <input value="${entity.limi}" required class="form-control" type="text" name="limi">
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
            $.ajax({
                type:'post',
                url:$(this).attr('action'),
                data:formData,
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

        })
    });
</script>
</body>
</html>