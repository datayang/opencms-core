<%@page import="org.opencms.ade.upload.CmsUploadActionElement"%><%
  CmsUploadActionElement upload = new CmsUploadActionElement(pageContext, request, response);
%><!DOCTYPE html>
<html>
  <head>
  	<%= upload.exportAll() %>
  	<title><%= upload.getTitle() %></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  <body class="-opencms" style="margin: 0px;">&nbsp;</body>
</html>
