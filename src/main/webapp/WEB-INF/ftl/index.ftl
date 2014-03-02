<html>
<head><title>FTL hello</title>
<body>
<div id="header">
    <H2>

        FTL sample
    </H2>
</div>

<div id="content">


    <br/>
    <table class="datatable">
        <tr>
            <th>Firstname</th>
        </tr>
    <#list model["userList"] as user>
        <tr>
            <td>${user}</td>
        </tr>
    </#list>
    </table>

</div>
</body>
</html>