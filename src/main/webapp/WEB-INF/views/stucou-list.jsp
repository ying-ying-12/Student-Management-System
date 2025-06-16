<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
  <title>选课列表</title>
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
              <div class="card-header"><h4>选课列表</h4></div>
              <div class="card-body">
                <form id="form" style="margin-bottom: 20px" class="form-inline" action="${pageContext.request.contextPath}/stucou" method="get">
                  <input type="hidden" id="current" name="current" value="1">
                  <div class="form-group">
                    <label >课程编号</label>
                    <input class="form-control" type="text" value="${cno}" name="cno">
                  </div>
                  <div class="form-group">
                    <label>学生学号</label>
                    <input class="form-control" type="text" value="${sno}" name="sno" placeholder="">
                  </div>
                  <div class="form-group">
                    <label>选课时间开始</label>
                    <input value="${chosetime1}" required class="form-control js-datepicker m-b-10" type="text"
                           name="chosetime1" placeholder="请选择时间" value="" data-date-format="yyyy-mm-dd" />
                  </div>
                  <div class="form-group">
                    <label>选课时间结束</label>
                    <input value="${chosetime2}" required class="form-control js-datepicker m-b-10" type="text"
                           name="chosetime2" placeholder="请选择时间" value="" data-date-format="yyyy-mm-dd" />
                  </div>

                  <div class="form-group">
                    <button class="btn btn-brown btn-round" type="submit">查询</button>
                  </div>
                </form>

                <table class="table table-bordered">
                  <thead>
                  <tr>
                    <th>#</th>
                    <th>学生</th>
                    <th>课程</th>
                    <th>选课时间</th>
                    <th>得分</th>
                    <th>评价</th>
                    <c:if test="${sessionScope.role == 'teacher'}">
                      <th>操作</th>
                    </c:if>
                    <c:if test="${sessionScope.role == 'student'}">
                      <th>退选</th>
                    </c:if>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${pagerVO.list}" var="i" varStatus="s">
                    <tr>
                      <th scope="row">${s.count}</th>
                      <td>${i.sno}/${i.sname}</td>
                      <td>${i.cno}/${i.cname}</td>
                      <td><fmt:formatDate value="${i.chosedate}" pattern="yyyy-MM-dd" /></td>
                      <td>${i.score}</td>
                      <td>${i.evaluation}</td>
                      <c:if test="${sessionScope.role == 'student'}">
                        <td>
                            <button class="btn btn-danger btn-xs" type="button" onclick="del('${i.cno}')">删除</button>
                        </td>
                      </c:if>
                      <c:if test="${sessionScope.role == 'teacher'}">
                        <td>
                            <%--教师只能编辑和删除自己课程--%>
                          <c:if test="${sessionScope.user.tno == i.tno}">
                            <button class="btn btn-primary btn-xs" type="button" onclick="location.href='?r=edit&cno=${i.cno}&sno=${i.sno}'">打分</button>
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
      url:'stucou?r=del',
      data:{cno},
      success:function (response) {
        if(response.success){
          lightyear.loading('hide');
          lightyear.url('stucou');
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
