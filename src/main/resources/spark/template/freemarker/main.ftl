<#-- @ftlvariable name="alive" type="java.lang.String" -->
<#-- @ftlvariable name="volume" type="java.lang.Number" -->
<#-- @ftlvariable name="list" type="java.util.Collection<com.cguillaume.omxcontrol.model.Mp3Metadata>" -->
<#-- @ftlvariable name="playing" type="java.lang.String" -->
<#-- @ftlvariable name="current" type="java.lang.Number" -->
<#-- @ftlvariable name="freeSpace" type="java.lang.String" -->
<#-- @ftlvariable name="lib" type="java.util.Collection<com.cguillaume.omxcontrol.model.Mp3Metadata>" -->
<#include 'track.ftl'>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, user-scalable=no">
    <title>Omx control</title>
    <script src="jquery-2.1.1.js"></script>
    <script src="script.js"></script>
	<script src="ui.js"></script>
    <link rel="stylesheet" href="style.css">
	<link rel="stylesheet" href="font-awesome.min.css">
</head>
<body>
<div id="actionbar">
	<a class="fa fa-play" onclick="pause()" tabindex="1"></a>
	<a class="fa fa-upload" onclick="toggleActionBar(uploadBar)" tabindex="1"></a>
	<a class="fa fa-youtube-play" onclick="toggleActionBar(youtubeBar)" tabindex="1"></a>
	V
	<input id="volume_range" type="range" max="100" min="1" step="3" value="${volume}"
		   oninput="updateVolume(this.value)" onchange="updateVolume(this.value)" tabindex="1">
	<span id="volume">${volume}%</span>
</div>
<div id="actionBarContainer">
	<div id="uploadBar">
		<input type="file" onchange="addFile(this)" tabindex="1">
	</div>
	<div id="youtubeBar">
		<div style="width: calc(100% - 48px);display: inline-block;">
			<span id="urlInput" contenteditable tabindex="1"
				  onkeydown="if(event.keyCode==13){event.preventDefault();add(this.textContent)}">
				YoutubeUrl
			</span>
		</div>
		<a style="width: 30px;display: inline-block;" onclick="add(urlInput.textContent)" tabindex="1">
			D
		</a>
	</div>
</div>
<div class="col">
	<h4>
		Library (${freeSpace})
	</h4>
	<div id="library">
	<#list lib as item>
		<@track item=item fct='addFromLib(${item_index})'/>
	</#list>
	</div>
</div>
<div class="col">
	<h4>
		Playlist
	</h4>
	<script>
		current = ${current};
		playing = ${playing};
		alive = ${alive};
	</script>
	<table id="playlist">
	<#list list as item>
		<tr>
			<td>
				<span class="fa
				<#if current == item_index>
					<#if alive == 'true'>
						<#if playing == 'true'>
							fa-play
						<#else>
							fa-pause
						</#if>
					</#if>
				</#if>
				"></span>
			</td>
			<td>
				<@track item=item fct='startAt(${item_index})'/>
			</td>
		</tr>
	</#list>
	</table>
</div>
<div class="col">
	<h4>
		Job(s)
	</h4>
	<div id="jobs">
	</div>
</div>
</body>
</html>
