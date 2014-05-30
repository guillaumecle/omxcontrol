function buildPlaylist(playlist) {
	var container = jQuery('#playlist');
	container.empty();
	var tr, td;
	jQuery.each(playlist.list, function(index, track) {
		tr = jQuery('<tr>');
		td = jQuery('<td>');
		if (index == playlist.current)
			td.text('▶');
		container.append(tr.append(td).append(jQuery('<td>').text(track)));
	});
}
function addFromLib(elem) {
    omxWS.sendAction('add', elem.textContent)
}
function pause() {
    omxWS.sendAction('pause');
}
function add(elem) {
    console.log(elem.previousSibling.previousSibling.value);
}
var omxWS = new WebSocket('ws://' + location.hostname + ':8080/');
omxWS.onmessage = function(msgEvent) {
	var response = JSON.parse(msgEvent.data);
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
omxWS.onclose = function(closeEvent) {
	if (confirm("Connexion lost\nDo you want to reload this page?")) {
		location.reload();
	}
};
omxWS.sendAction = function(action, message) {
	this.send(JSON.stringify({action : action, message : JSON.stringify(message)}));
};
playlistUpdated = buildPlaylist;
updateCurrent = function(current) {
	jQuery('#playlist').find('td:first-child').each(function(index, td) {
		td.textContent = index == current ? '▶' : '';
	});
};