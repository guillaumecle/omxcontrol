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
    <title>Omx control</title>
    <script src="jquery-2.1.1.js"></script>
    <script src="script.js"></script>
    <link rel="stylesheet" href="style.css">
</head>
<body>
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
		alive = ${alive};
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
				<@track item=item fct=''/>
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