<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/10/15
  Time: 14:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="fm" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<fm:form modelAttribute="user" method="post" >
    <div>登录名：<fm:input path="userCode" /><fm:errors path="userCode"/></div>
    <div>用户名：<fm:input path="userName" /></div>
    <div>密码：<fm:password path="userPassword" /><fm:errors path="userPassword"/></div>
    <div>用户性别：<fm:radiobutton path="gender" value="1" />男<fm:radiobutton path="gender"  value="0"/>女</div>
    <div> 出生日期：<fm:input path="birthday"  /></div>
    <div>电话：<fm:password path="phone" /></div>
    <div>角色：<fm:select path="userRole">
        <fm:option value="1">管理员</fm:option>
        <fm:option value="2">经理</fm:option>
        <fm:option value="3">普通员工</fm:option>
    </fm:select></div>
    <div>
        <fm:button value="提交" >提交</fm:button>
    </div>
</fm:form>
<script src="/statics/calendar/WdatePicker.js"></script>
</body>
</html>
