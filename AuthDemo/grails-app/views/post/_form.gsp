<%@ page import="org.authdemo.Post" %>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="post.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${postInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'content', 'error')} ">
	<label for="content">
		<g:message code="post.content.label" default="Content" />
		
	</label>
	<g:textArea name="content" cols="40" rows="5" maxlength="1000" value="${postInstance?.content}"/>
</div>

<sec:ifAllGranted roles="ROLE_ADMIN">
<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="post.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${org.authdemo.User.list()}" optionKey="id" required="" value="${postInstance?.user?.id}" class="many-to-one"/>
</div>
</sec:ifAllGranted>

<sec:ifNotGranted roles="ROLE_ADMIN">
	<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'user', 'error')} required">
			<g:hiddenField name="user.id" value="${sec.loggedInUserInfo(field: "id")}" />
	</div>
</sec:ifNotGranted>
