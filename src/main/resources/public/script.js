// ************* Model ***************
var current;
var playing;
// *************** UI ****************
function reBuildPlaylist(playlist) {
	var container = jQuery('#playlist');
	container.empty();
	jQuery.each(playlist, function(index, track) {
		var tr = jQuery('<tr>');
		var td = jQuery('<td>');
		if (index == current)
			td.text(playing ? '▶' : 'P');
		var a = jQuery('<a>').text(track);
		a.onclick = function(event) {
			console.log(event);
		};
		container.append(tr.append(td).append(jQuery('<td>').append(a)));
	});
}
function rebuildPlayIcon() {
	jQuery('#playlist').find('td:first-child').each(function(index, td) {
		td.textContent = index == current ? playing ? '▶' : 'P' : '';
	});
}
// ************ player control *********
function addFromLib(elem) {
    omxWS.sendAction('add', elem.textContent)
}
function pause() {
    omxWS.sendAction('pause');
}
function add(elem) {
    console.log(elem.previousSibling.previousSibling.value);
}
// ******* websocket api *****************
var omxWS = new WebSocket('ws://' + location.hostname + ':8080/');
omxWS.onmessage = function(msgEvent) {
	var response = JSON.parse(msgEvent.data);
	console.log(response);
	if (response.action && response.message) {
		if (window[response.action]) {
			window[response.action](JSON.parse(response.message));
		} else {
			throw "No handler define for this action (" + response.action + ")";
		}
	} else {
		throw "Incorrect message : " + response;
	}
};
omxWS.onclose = function() {
	if (confirm("Connexion lost\nDo you want to reload this page?")) {
		location.reload();
	}
};
omxWS.sendAction = function(action, message) {
	this.send(JSON.stringify({action : action, message : JSON.stringify(message)}));
};
// ********* websocket handler **************
playlistUpdated = reBuildPlaylist;
updateCurrent = function(current) {
	window.current = current;
	rebuildPlayIcon();
};
playingChanged = function(playing) {
	window.playing = playing;
	rebuildPlayIcon();
};