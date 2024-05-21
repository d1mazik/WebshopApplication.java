<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Login</h1>
    <hr>
    <form action="/login" method="post">
        <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" class="form-control <#if formSubmitted?exists && (RequestParameters.error!false)>is-invalid</#if>" id="email" name="username">
            <div class="invalid-feedback">
                Invalid e-mail
            </div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control <#if formSubmitted?exists && (RequestParameters.error!false)>is-invalid</#if>" id="password" name="password">
            <div class="invalid-feedback">
                Invalid password
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-dark">Login</button>
    </form>
    <hr>
    <h2>No account? <a href="/registration">Register!</a></h2>
</@t.template>
