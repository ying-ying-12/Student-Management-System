<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>新增课程</title>
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
                <form id="myForm" action="${pageContext.request.contextPath}/course?r=add" method="post">
                  <div class="form-group">
                    <label for="cno">课程编号</label>
                    <input required class="form-control" type="text" name="cno" id="cno" aria-label="课程编号" placeholder="请输入课程编号">
                  </div>
                  <div class="form-group">
                    <label for="cname">课程名</label>
                    <input required class="form-control" type="text" name="cname" id="cname" aria-label="课程名" placeholder="请输入课程名">
                  </div>
                  <div class="form-group">
                    <label for="begindate">选课开始时间</label>
                    <input required class="form-control js-datetimepicker" type="text"
                           name="begindate" id="begindate" placeholder="请选择具体时间" data-side-by-side="true" data-locale="zh-cn" date-format="yyyy-MM-DD HH:mm:ss" aria-label="选课开始时间" />
                  </div>
                  <div class="form-group">
                    <label for="enddate">选课结束时间</label>
                    <input required class="form-control js-datetimepicker" type="text"
                           name="enddate" id="enddate" placeholder="请选择具体时间" data-side-by-side="true" data-locale="zh-cn" date-format="yyyy-MM-DD HH:mm:ss" aria-label="选课结束时间" />
                  </div>
                  <div class="form-group">
                    <label for="credits">学分</label>
                    <input class="form-control" type="number" name="credits" id="credits" aria-label="学分" min="0" max="20" step="0.5">
                  </div>
                  <div class="form-group">
                    <label for="limi">限制人数</label>
                    <input required class="form-control" type="number" name="limi" id="limi" aria-label="限制人数" min="1">
                  </div>

                  <div class="form-group">
                    <button class="btn btn-primary" type="submit" aria-label="提交课程信息">提交</button>
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