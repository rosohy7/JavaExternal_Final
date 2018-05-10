<html>
<body>
<h2> Sign Up Form </h2>
<form action="/bankapp/dispatcher" method="POST">
<input type="hidden" name="action" value="SignUp"/>
Login name: <input type="textbox" name="login"/><br/>
Password: <input type="password" name="password"/><br/>
Repeat password: <input type="password" name="repassword"/><br/>
<input type="submit" value="Sign Up"/><br/>
</form>
</body>
</html>