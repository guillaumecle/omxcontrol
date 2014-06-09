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
function addJob(job) {
	var div = jQuery('<div>').attr('id','job' + job.id)
		.append(jQuery('<span>').text(job.name))
		.append(jQuery('<progress>').attr('max', 100));
	jQuery('#jobs').append(div);
}
function updateJob(job) {
	jQuery('#job'+job.id).find('progress').attr('value', job.progress);
}
function setVolume(volume) {
	jQuery('#volume').text(volume + '%');
}
// *************** control *************
function addFromLib(elem) {
    omxWS.sendAction('add', elem.textContent)
}
function pause() {
    omxWS.sendAction('pause');
}
function add(elem) {
	var job = {
		name : 'Download of ' + elem.previousSibling.previousSibling.value,
		id : Math.round(new Date().getTime() * Math.random()),
		url : elem.previousSibling.previousSibling.value
	};
	omxWS.sendAction('dlFromYoutube', job);
	addJob(job);
}
function addFile(elem) {
	var fileFilst = elem.files;
	if (fileFilst.length > 0) {
		var job = {
			jsFile : fileFilst[0],
			name : 'Upload of ' + fileFilst[0].name,
			id : Math.round(new Date().getTime() * Math.random())
		};
		addJob(job);
		omxWS.sendAction("uploadFile", job);
		omxWS.send(fileFilst[0]);
	}
}
// ******* websocket api *****************
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
uploadCompleted = function(job) {
	var li = jQuery('<li>').append(jQuery('<a>').text(job.jsFile.name).click(function() {
		addFromLib(this);
	}));
	updateJob(job);
	jQuery('#library').append(li);
};
downloadProgress = updateJob;
volumeUpdated = setVolume;