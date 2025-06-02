

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!--左侧导航-->
<aside class="lyear-layout-sidebar">

    <!-- logo -->
    <div id="logo" class="sidebar-header">
        <a href="${pageContext.request.contextPath}/index.jsp"><img src="${pageContext.request.contextPath}/assets/images/logo-sidebar.png" title="LightYear" alt="LightYear" /></a>
    </div>
    <div class="lyear-layout-sidebar-scroll">

        <nav class="sidebar-main">
            <ul class="nav nav-drawer">
                <li class="nav-item active"> <a href="${pageContext.request.contextPath}/"><i class="mdi mdi-home"></i> 后台首页</a> </li>
                <li class="nav-item nav-item-has-subnav open">
                    <a href="javascript:void(0)"><i class="mdi mdi-format-align-justify"></i> 功能</a>
                    <ul class="nav nav-subnav">
                        <li> <a href="${pageContext.request.contextPath}/student">学生信息</a> </li>
                        <li> <a href="${pageContext.request.contextPath}/clazz">班级信息</a> </li>
                    </ul>
                </li>

            </ul>
        </nav>

        <div class="sidebar-footer">
            <p class="copyright">Copyright &copy; 2019. <a target="_blank" href="http://lyear.itshubao.com">IT书包</a> All rights reserved.</p>
        </div>
    </div>

</aside>
<!--End 左侧导航-->

<!--头部信息-->
<header class="lyear-layout-header">

    <nav class="navbar navbar-default">
        <div class="topbar">

            <div class="topbar-left">
                <div class="lyear-aside-toggler">
                    <span class="lyear-toggler-bar"></span>
                    <span class="lyear-toggler-bar"></span>
                    <span class="lyear-toggler-bar"></span>
                </div>
                <span class="navbar-page-title">  </span>
            </div>

            <ul class="topbar-right">
                <li class="dropdown dropdown-profile">
                    <a href="javascript:void(0)" data-toggle="dropdown">
                        <img class="img-avatar img-avatar-48 m-r-10" src="${pageContext.request.contextPath}/assets/images/users/avatar.jpg" alt="笔下光年" />
                        <span>${sessionScope.role == 'admin'? sessionScope.user.username: sessionScope.user.name} <span class="caret"></span></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <c:if test="${sessionScope.role == 'student'}">
                            <li> <a href="#" data-toggle="modal" data-target="#exampleModal1" data-whatever="个人信息"><i class="mdi mdi-account"></i> 个人信息</a> </li>
                            <li> <a href="#" data-toggle="modal" data-target="#exampleModal2" data-whatever="修改密码"><i class="mdi mdi-lock-outline"></i> 修改密码</a> </li>
                            <li class="divider"></li>
                        </c:if>
                        <li> <a href="${pageContext.request.contextPath}/logout"><i class="mdi mdi-logout-variant"></i> 退出登录</a> </li>
                    </ul>
                </li>
                <!--切换主题配色-->
                <li class="dropdown dropdown-skin">
                    <span data-toggle="dropdown" class="icon-palette"><i class="mdi mdi-palette"></i></span>
                    <ul class="dropdown-menu dropdown-menu-right" data-stopPropagation="true">
                        <li class="drop-title"><p>主题</p></li>
                        <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="site_theme" value="default" id="site_theme_1" checked>
                    <label for="site_theme_1"></label>
                  </span>
                            <span>
                    <input type="radio" name="site_theme" value="dark" id="site_theme_2">
                    <label for="site_theme_2"></label>
                  </span>
                            <span>
                    <input type="radio" name="site_theme" value="translucent" id="site_theme_3">
                    <label for="site_theme_3"></label>
                  </span>
                        </li>
                        <li class="drop-title"><p>LOGO</p></li>
                        <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="logo_bg" value="default" id="logo_bg_1" checked>
                    <label for="logo_bg_1"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_2" id="logo_bg_2">
                    <label for="logo_bg_2"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_3" id="logo_bg_3">
                    <label for="logo_bg_3"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_4" id="logo_bg_4">
                    <label for="logo_bg_4"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_5" id="logo_bg_5">
                    <label for="logo_bg_5"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_6" id="logo_bg_6">
                    <label for="logo_bg_6"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_7" id="logo_bg_7">
                    <label for="logo_bg_7"></label>
                  </span>
                            <span>
                    <input type="radio" name="logo_bg" value="color_8" id="logo_bg_8">
                    <label for="logo_bg_8"></label>
                  </span>
                        </li>
                        <li class="drop-title"><p>头部</p></li>
                        <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="header_bg" value="default" id="header_bg_1" checked>
                    <label for="header_bg_1"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_2" id="header_bg_2">
                    <label for="header_bg_2"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_3" id="header_bg_3">
                    <label for="header_bg_3"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_4" id="header_bg_4">
                    <label for="header_bg_4"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_5" id="header_bg_5">
                    <label for="header_bg_5"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_6" id="header_bg_6">
                    <label for="header_bg_6"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_7" id="header_bg_7">
                    <label for="header_bg_7"></label>
                  </span>
                            <span>
                    <input type="radio" name="header_bg" value="color_8" id="header_bg_8">
                    <label for="header_bg_8"></label>
                  </span>
                        </li>
                        <li class="drop-title"><p>侧边栏</p></li>
                        <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="sidebar_bg" value="default" id="sidebar_bg_1" checked>
                    <label for="sidebar_bg_1"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_2" id="sidebar_bg_2">
                    <label for="sidebar_bg_2"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_3" id="sidebar_bg_3">
                    <label for="sidebar_bg_3"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_4" id="sidebar_bg_4">
                    <label for="sidebar_bg_4"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_5" id="sidebar_bg_5">
                    <label for="sidebar_bg_5"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_6" id="sidebar_bg_6">
                    <label for="sidebar_bg_6"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_7" id="sidebar_bg_7">
                    <label for="sidebar_bg_7"></label>
                  </span>
                            <span>
                    <input type="radio" name="sidebar_bg" value="color_8" id="sidebar_bg_8">
                    <label for="sidebar_bg_8"></label>
                  </span>
                        </li>
                    </ul>
                </li>
                <!--切换主题配色-->
            </ul>

        </div>
    </nav>

</header>
<!--End 头部信息-->

<c:if test="${sessionScope.role == 'student'}">
    <div class="modal fade" id="exampleModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel1">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="exampleModalLabel1">个人中心</h4>
                </div>
                <form id="studentPersonal" action="${pageContext.request.contextPath}/student-personal?r=edit" method="post">
                    <div class="modal-body">
                        <div class="form-group">
                            <label>学号</label>
                            <input required readonly class="form-control" type="text" name="sno"
                                   value="${sessionScope.user.sno}">
                        </div>
                        <div class="form-group">
                            <label>姓名</label>
                            <input required class="form-control" type="text" name="name"
                                   value="${sessionScope.user.name}">
                        </div>
                        <div class="form-group">
                            <label>电话</label>
                            <input maxlength="11" class="form-control" type="number" name="tele"
                                   value="${sessionScope.user.tele}">
                        </div>
                        <div class="form-group">
                            <label>入学时间</label>
                            <input class="form-control js-datepicker m-b-10" type="text" name="enterdate" placeholder="yyyy-mm-dd"
                                   value="${sessionScope.user.enterdate}" data-date-format="yyyy-mm-dd"/>
                        </div>
                        <div class="form-group">
                            <label>年龄</label>
                            <input class="form-control" type="number" name="age" value="${sessionScope.user.age}">
                        </div>
                        <div class="form-group">
                            <label>性别</label>
                            <div class="col-xs-12">
                                <div class="radio">
                                    <label for="example-radio1">
                                        <input <c:if test="${sessionScope.user.gender=='m'}">checked</c:if>
                                               type="radio" id="example-radio1" name="gender" value="m">男
                                    </label>
                                </div>
                                <div class="radio">
                                    <label for="example-radio2">
                                        <input <c:if test="${sessionScope.user.gender=='w'}">checked</c:if>
                                               type="radio" id="example-radio2" name="gender" value="w">女
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>详细地址</label>
                            <textarea class="form-control" name="address">${sessionScope.user.address}</textarea>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <input type="submit" class="btn btn-primary" value="提交">
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="exampleModal2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel2">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="exampleModalLabel2">新消息</h4>
                </div>
                <form id="studentPassword" action="${pageContext.request.contextPath}/student-personal?r=pwd" method="post">
                    <div class="modal-body">
                        <input readonly class="form-control" type="hidden" name="sno" value="${sessionScope.user.sno}">
                        <div class="form-group">
                            <label>旧密码</label>
                            <input required class="form-control" type="password" name="oldPwd">
                        </div>
                        <div class="form-group">
                            <label>新密码</label>
                            <input required class="form-control" type="password" name="newPwd">
                        </div>
                        <div class="form-group">
                            <label>重复新密码</label>
                            <input required class="form-control" type="password" name="newPwd2">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="submit" class="btn btn-primary">确认修改</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

</c:if>
