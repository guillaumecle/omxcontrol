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
</head>
<body>
<div id="actionbar">
	<a onclick="pause()" tabindex="1">&#9654;</a>
	<a onclick="toggleActionBar(uploadBar)" tabindex="1">U</a>
	<a onclick="toggleActionBar(youtubeBar)" tabindex="1">Y</a>
	V
	<input id="volume_range" type="range" max="100" min="1" step="3" value="${volume}"
		   oninput="updateVolume(this.value)" onchange="updateVolume(this.value)" tabindex="1">
	<span id="volume">${volume}%</span>
</div>
<div id="actionBarContainer">
	<div id="uploadBar" style="font-size: 28px">
		<table>
			<td style="width:2px"></td>
			<td style="font-size: 28px">
				<input type="file" onchange="addFile(this)" tabindex="1">
			</td>
			<td style="width:2px"></td>
		</table>
	</div>
	<div id="youtubeBar">
		<table>
			<td style="font-size: 28px">
				<span id="urlInput" contenteditable tabindex="1"
					  onkeydown="if(event.keyCode==13){event.preventDefault();add(this.textContent)}">
					YoutubeUrl
				</span>
			</td>
			<td style="width: 40px;text-align: center">
				<a onclick="add(urlInput.textContent)" tabindex="1">D</a>
			</td>
		</table>
	</div>
</div>
<#--<div id="actionbarbackground"></div>-->
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
				<#if current == item_index>
					<#if playing == 'true' && alive == 'true'>
						&#9654;
					<#else>
						P
					</#if>
				</#if>
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
