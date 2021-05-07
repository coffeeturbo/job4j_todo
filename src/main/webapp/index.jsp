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
        .new-item tr td {
            text-align: left;
        }

        tr td {
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="span10">
            <form class="well form-horizontal new-item">
                <h3>Новая задача</h3>
                <table>
                    <tr>
                        <td><label class="control-label" for="desc">Введите задачу</label></td>
                        <td><input type="text" class="input-xlarge" id="desc" name="desc" placeholder="Что нужно сделать?">
                        </td>
                    </tr>
                    <tr>
                        <td><label class="control-label" for="category_select">Выберите категорию</label></td>
                        <td>
                            <select name="category" id="category_select" multiple>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button type="submit" id="createTodo" class="btn btn-success"><i
                                    class="bi bi-plus-circle-fill"></i> добавить
                            </button>
                        </td>
                        <td>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="span10">
            <h2>Список дел</h2>
            <label class="checkbox">
                <input type="checkbox" name="filter" id="filter"> показать все
            </label>
            <table class="table table-bordered table-striped table-condensed task-list">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Категории</th>
                    <th>Описание</th>
                    <th>Дата создания</th>
                    <th>Создатель</th>
                    <th>Выполнено</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
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
<script>

    $(document).ready(function () {
        listCategories();
        listItems();

        $("#filter").on("change", function (e) {
            if ($('#filter').is(":checked")) {
                listItems("any")
            } else {
                listItems("true")
            }
        });

        $("#createTodo").on("click", function (e) {
            createItem();
            e.preventDefault();
        });
    });

    function createDoneButton(item) {
        let done;
        switch (item.done) {
            case true :
                done = '<button class="btn btn-success undone" onclick="updateItem(' + item.id + ', \'false\')"><i class="bi bi-check-square"></i></button> ';
                break;
            default :
                done = '<button class="btn btn-success done" onclick="updateItem(' + item.id + ', \'true\')"><i class="bi bi-square"></i></button> ';
        }
        return done;
    }

    function createTrHtml(item) {

        let rows;
        let categories = (function (categories) {
            let catNames = categories.map(category => category.name);
            let cats = catNames.join(", ");

            return cats;
        })(item.categories);

        rows = rows + "<tr class='item-" + item.id + "'><td>"
            + item.id + "</td>"
            + "<td>" + categories + "</td>"
            + "<td>" + item.description + "</td><td>"
            + item.created + "</td>";

        rows = item.user ? rows + "<td>" + item.user.email + "</td>" : rows + "<td> нет </td>";

        rows = rows + "<td> " + createDoneButton(item) + "</td> ";
        rows = rows
            + "<td>" + '<button type="submit" onclick="deleteItem(' + item.id + ')" class="btn btn-danger"><i class="bi bi-x-circle-fill"></i></button>' + "</td>"
            + "</tr>";

        return rows;
    }

    function listCategories() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/job4j_todo_war/categories',
            dataType: 'json'
        }).done(function (data) {
            let rows;
            let body = $("#category_select");
            body.html('');
            $(data).each(function (i, category) {
                rows = rows + "<option value='" + category.id + "' >" + category.name + "</option>"
            });
            body.html(rows)

        }).fail(function (err) {
            console.log(err);
        });
    }

    function listItems(done = "false") {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/job4j_todo_war/items',
            dataType: 'json',
            data: "done=" + done
        }).done(function (data) {
            let rows;
            let body = $(".task-list tbody");
            body.html('');
            $(data).each(function (i, item) {
                rows = rows + createTrHtml(item)
            });
            body.html(rows)

        }).fail(function (err) {
            console.log(err);
        });
    }

    function updateItem(id, done) {
        var item = {
            done: done
        }

        jQuery.ajax({
            type: 'POST',
            url: 'http://localhost:8080/job4j_todo_war/items?id=' + id,
            dataType: 'json',
            data: "data=" + JSON.stringify(item)
        }).done(function (item) {
            console.log(item);
            $(".task-list tr.item-" + item.id).replaceWith(createTrHtml(item))
        }).fail(function (err) {
            console.log(err);
        });

        return false;
    }

    function createItem() {

        let categories = [];
        $("#category_select option:selected").each(function () {
            categories.push($(this).val())
        });

        let item = {
            description: $("#desc").val(),
            categories: categories,
            done: false
        }

        console.log(item);

        jQuery.ajax({
            type: 'POST',
            url: 'http://localhost:8080/job4j_todo_war/items',
            dataType: 'json',
            data: "data=" + JSON.stringify(item)
        }).done(function (item) {
            console.log(item);

            $(".task-list tbody").append(createTrHtml(item));

        }).fail(function (err) {
            console.log(err);
        });

        return false;
    }

    function deleteItem(id) {

        jQuery.ajax({
            type: 'DELETE',
            url: 'http://localhost:8080/job4j_todo_war/items?' + "id=" + id,
            dataType: 'json'
        }).done(function (data) {
            console.log(data);
            $(".item-" + id).remove();
        }).fail(function (err) {
            console.log(err);
        });

        return false;
    }
</script>
</body>
</html>
