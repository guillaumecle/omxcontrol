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
Library
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
<ul>
<#list list as item>
    <li>
        ${item}
    </li>
</#list>
</ul>
</body>
</html>