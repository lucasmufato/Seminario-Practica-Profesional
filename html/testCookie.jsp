<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Testeo de cookies</title>
</head>
<body>
    <div align="center">
        <h3>COOKIES</h3>
         <br>
        <%
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("nombre_usuario")){
						Cookie c = cookie;                    	
						%>
                    	<p>Nombre: <%=c.getName()%></p>
                    	<p>Valor: <%=c.getValue()%></p>
                    	<p>MaxAge: <%=c.getMaxAge()%></p>
                    	<br>
                    	<%
						}
                }
            }
        %>
        <br>
    </div>
</body>
</html>