<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav style="height: 60px">
    <ul class="pagination" style="float: right;margin: 0">
        <li <c:if test="${!pagerVO.showLeft}">class="disabled"</c:if> >
            <a href="javascript:gotoPage(${pagerVO.current - 1})">
                <span><i class="mdi mdi-chevron-left"></i></span>
            </a>
        </li>
        <c:forEach items="${pagerVO.pageNums}" var="i" varStatus="s">
            <li <c:if test="${i == pagerVO.current}">class="active"</c:if> >
                <a href="javascript:gotoPage(${i})">${i}</a>
            </li>
        </c:forEach>
        <li <c:if test="${!pagerVO.showRight}">class="disabled"</c:if>>
            <a href="javascript:gotoPage(${pagerVO.current + 1}">
                <span><i class="mdi mdi-chevron-right"></i></span>
            </a>
        </li>
    </ul>
</nav>
