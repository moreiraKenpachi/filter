<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
		
	<title>Curso JSP</title>
	
<style type="text/css">

form{
 position: absolute;
 top: 30%;
 left: 33%;
 right: 33%;
}

h4{
 position: absolute;
 top: 20%;
 left: 33%;
 right: 33%;
}

.msg{
 position: absolute;
 top: 12%;
 left: 33%;
 right: 33%;
 font-size: 15px;
 	color: #842029;
    background-color: #f8d7da;
    border-color: #f5c2c7;
    	
    padding-left: 5rem;
}

</style>
</head>
<body>
<h4>Bem-vindo ao curso de JSP</h4>
<%
	out.print("sucesso garantido...");
%>

	<!-- Envia dados para a Servlet -->
<form action="<%= request.getContextPath() %>/ServletLogin" method="post" class="row g-3 needs-validation" novalidate>
<input type="hidden" value="<%= request.getParameter("url") %>" name="url">

<div class="mb-3">
 <label class="form-label">Login</label>
 <input class="form-control" name="Login" type="text" style="background: grey;" required="required">
 <div class="invalid-feedback">
      informe o login!
    </div>
  <div class="valid-feedback">
      Ok!
    </div>
</div>
<div class="mb-3">
 <label class="form-label">Senha</label>
 <input class="form-control" name="senha" type="password" style="background: grey;" required="required">
 <div class="invalid-feedback">
     informe a senha!
    </div>
  <div class="valid-feedback">
      Ok!
    </div>
</div>

 <input class="btn btn-primary" type="submit" value="Acessar" style="background: blue;">

</form>
<h4 class="msg">${msg}</h4>


	<!-- Option 1: Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

<script type="text/javascript">

//Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.prototype.slice.call(forms)
    .forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }

        form.classList.add('was-validated')
      }, false)
    })
})()

</script>

</body>
</html>