<#macro track item fct>
<div class="track" onclick="${fct}" tabindex="1">
	<#if item.coverURI??!>
		<img src="${item.coverURI}">
	</#if>
	${item.title}
	<#if item.artist??!>
		<br>
	${item.artist}
	</#if>
</div>
</#macro>