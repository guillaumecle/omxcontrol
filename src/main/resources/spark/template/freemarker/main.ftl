<!doctype html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="src/main/resources/public/style.css"/>
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
    <li>
        <a onclick="addFromLib(this)">${item}</a>
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
	        <a onclick="startFrom(${item_index})">${item}</a>
		</td>
    </tr>
</#list>
</table>
<a onclick="pause()">&#9654; play/pause</a>
<h4>
	Job(s)
</h4>
<div id="jobs">
</div>
</body>
</html>