<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Omx control</title>
    <script src="jquery-1.11.1.min.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style.css">
</head>
<body>
Library (${freeSpace})
<ul>
<#list lib as item>
    <li>
        <a onclick="addFromLib(this)">${item}</a>
    </li>
</#list>
</ul>
<p>
<input/>
<a onclick="add(this)">Add to playlist</a>
</p>
Playlist
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
</body>
</html>