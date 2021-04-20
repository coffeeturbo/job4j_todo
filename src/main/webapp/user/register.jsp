<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Todo application</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css">

    <style type="text/css">
        tr td {
            text-align: center;
        }
    </style>


</head>
<body>

<div class="container">
    <div class="row">
        <div class="span10">
            <fieldset>
                <div class="control-group">
                    <h3>Регистрация</h3>

                    <div>
                        <form class="well form-inline" action="<%=request.getContextPath()%>/register.do" method="post">
                            <input type="text" class="input-small" placeholder="Email" id="email" name="email" >
                            <input type="password" class="input-small" placeholder="Password" id="password" name="password">
                            <button type="submit"
                                    class="btn btn-success"><i
                                    class="bi bi-plus-circle-fill"
                            ></i> Войти
                            </button>
                        </form>
                    </div>
                </div>
            </fieldset>
        </div>
    </div>

</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"
        integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
        integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
        integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
        crossorigin="anonymous"></script>


</body>
</html>
