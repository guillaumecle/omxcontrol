<#-- @ftlvariable name="volume" type="java.lang.Number" -->
<#-- @ftlvariable name="list" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="playing" type="java.lang.Boolean" -->
<#-- @ftlvariable name="current" type="java.lang.Number" -->
<#-- @ftlvariable name="freeSpace" type="java.lang.String" -->
<#-- @ftlvariable name="lib" type="java.util.Collection<com.cguillaume.omxcontrol.model.Mp3Metadata>" -->
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
    <title>Omx control</title>
    <script src="jquery-2.1.1.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h4>
	Library (${freeSpace})
</h4>
<ul id="library">
<#list lib as item>
    <li onclick="addFromLib(this)" id="${item.filePath}">
		<#if item.title??!>
			${item.title}
			<#if item.artist??!>
				<br>
				${item.artist}
			</#if>
		<#else>
			${item.filePath}
		</#if>
    </li>
</#list>
</ul>
<p>
	<input type="file" onchange="addFile(this)">
</p>
<p>
	<input/>
	<a onclick="add(this)">Add to playlist</a>
</p>
<h4>
	Playlist
</h4>
<script>
	current = ${current};
	playing = ${playing};
</script>
<table id="playlist">
<#list list as item>
    <tr>
        <td>
            <#if current == item_index>
				<#if playing == 'true'>
                	&#9654;
				<#else>
					P
				</#if>
        	</#if>
        </td>
		<td>
	        <a>${item}</a>
		</td>
    </tr>
</#list>
</table>
<a onclick="pause()">&#9654; play/pause</a>
<p>
	Volume : <span id="volume">${volume}%</span>
	<button onclick="omxWS.sendAction('changeVolume', false)">-</button>
	<button onclick="omxWS.sendAction('changeVolume', true)">+</button>
	<span></span>
</p>
<h4>
	Job(s)
</h4>
<div id="jobs">
</div>
</body>
</html>