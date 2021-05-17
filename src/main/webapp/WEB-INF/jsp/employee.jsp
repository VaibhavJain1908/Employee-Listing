<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: vaibhavjain03
  Date: 12-05-2021
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Employee</title>
    <spring:url value="/resources/css/employee.css" var="mainCss" />
    <link href="${mainCss}" rel="stylesheet" />
    <spring:url value="/resources/js/employee.js" var="mainJs" />
    <script src="${mainJs}"></script>
    <spring:url value="/resources/js/date.js" var="dateJs" />
    <script src="${dateJs}"></script>
</head>
<body>

<div>

    <form action='/${link}' class='form' method="post">

        <p class="field half"></p>
        <p class="field half">
            <label class="label">Welcome ${user} | <a href="/login" style="color:red; text-decoration: none">Log Out</a></label>
        </p>

        <p class='field half required'>
            <label class='label' for='code'>Employee Code</label>
            <input class='text-input' id='code' name='code' type='text' value='${code}' ${readOnly}>
        </p>
        <p class='field required half'>
            <label class='label required' for='name'>Employee Name</label>
            <input class='text-input' id='name' name='name' required type='text' value='${name}'>
        </p>
        <p class='field half'>
            <label class='label' for='location'>Location</label>
            <input class='text-input' id='location' name='location' type='text' value='${location}'>
        </p>
        <p class='field required half'>
            <label class='label' for='email'>E-mail</label>
            <input class='text-input' id='email' name='email' required type='email' value='${email}'>
        </p>
        <p class='field half'>
            <label class='label' for='date'>Date of Birth</label>
            <input class='text-input' id='date' name='date' type='text' onblur= 'ValidateDOB()' value='${date}' /><br>
            <span style="color: red" class="error" id="lblError"></span>
        </p>
        <p class='field half'>
            <input class='button' type='submit' value='${button}'>
        </p>
    </form>
</div>

</body>
</html>
