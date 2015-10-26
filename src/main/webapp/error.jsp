<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="/css/bootstrap-theme.min.css" type="text/css" rel="stylesheet"/>
    <link href="/css/site.css" type="text/css" rel="stylesheet"/>
</head>

<body>
<div class="panel panel-default">
    <div class="panel-heading">
        Error
    </div>
    <div class="panel-body">
        <div class="error">${error}</div>
        <div><a href="/">Return to the home page</a></div>
        <%
        Exception exception = (Exception)request.getAttribute("exception");
        if(exception != null) {
            %><div class="technicaldetails"><%
            final StackTraceElement[] stackTrace = exception.getStackTrace();
            for(StackTraceElement element : stackTrace) {
                %><div><%= element.toString() %><%
            }
            %></div><%
        }
        %>
    </div>
</div>

<script type="text/javascript" src="/js/jquery-2.1.4.js"></script>
<script type="text/javascript" src="/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/bootstrap-filestyle.js"></script>
</body>
</html>