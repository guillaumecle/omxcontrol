// ************* Struc ***************
function Track(filePath, album, artist, title, track, coverURI) {
	this.filePath = filePath;
	this.album = album;
	this.artist = artist;
	this.title = title;
	this.track = track;
	this.coverURI = coverURI;
}
// ************* Model ***************
var current;
var playing;
var alive;
// *************** UI ****************
function createTrackDiv(track, onClick) {
	var trackDiv = jQuery('<div>')
		.attr('class', 'track')
		.attr('tabindex', 1);
	if (track.coverURI) {
		trackDiv.append(jQuery('<img>').attr('src', track.coverURI));
	}
	trackDiv.append(document.createTextNode(track.title));
	if (track.artist) {
		trackDiv.append(jQuery('<br>'));
		trackDiv.append(document.createTextNode(track.artist));
	}
	trackDiv.click(onClick);
	return trackDiv;
}
function reBuildPlaylist(playlist) {
	var container = jQuery('#playlist');
	container.empty();
	playlist.forEach(function(track, index) {
		var tr = jQuery('<tr>');
		var span = jQuery('<span>').addClass('fa');
		if (index == current && alive) {
			span.addClass(playing ? 'fa-play' : 'fa-pause');
		}
		var td = jQuery('<td>').append(span);
		var trackDiv = createTrackDiv(track, function() {
			startAt(index);
		});
		container.append(tr.append(td).append(jQuery('<td>').append(trackDiv)));
	});
}
function reBuildLibrary(library) {
	var container = jQuery('#library');
	container.empty();
	library.forEach(function (track, index) {
		container.append(createTrackDiv(track, function() {
			addFromLib(index);
		}));
	});
}
function rebuildPlayIcon() {
	jQuery('#playlist').find('td:first-child').each(function(index, td) {
		var span = jQuery(td.firstChild);
		span.removeClass('fa-play fa-pause');
		if (index == current && alive) {
			span.addClass(playing ? 'fa-play' : 'fa-pause');
		}
	});
}
function addJob(job) {
	var div = jQuery('<div>').attr('id','job' + job.id)
		.append(jQuery('<span>').attr('class', 'name').text(job.name))
		.append(jQuery('<progress>').attr('max', 100))
		.append(jQuery('<span>').attr('class', 'status').text('Status : ' + job.status));
	jQuery('#jobs').append(div);
}
function updateJob(job) {
	var jobDiv = jQuery('#job'+job.id);
	jobDiv.find('progress').attr('value', job.progress);
	jobDiv.find('.status').text('Status : ' + job.status);
	jobDiv.find('.name').text(job.name);
}
function setVolume(volume) {
	jQuery('#volume').text(volume + '%');
	document.getElementById('volume_range').value = volume;
}
// *************** control *************
function startAt(index) {
	omxWS.sendAction('startAt', index);
}
function addFromLib(index) {
    omxWS.sendAction('add', index);
}
function pause() {
    omxWS.sendAction('pause');
}
function add(url) {
	var job = {
		name : 'Download of ' + url,
		id : Math.round(new Date().getTime() * Math.random()),
		url : url,
		status : 'UNINITIALIZED'
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
			id : Math.round(new Date().getTime() * Math.random()),
			status : 'UNINITIALIZED'
		};
		addJob(job);
		omxWS.sendAction("uploadFile", job);
		omxWS.send(fileFilst[0]);
	}
}
function updateVolume(value) {
	omxWS.sendAction('setVolume', value);
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
libraryUpdated = reBuildLibrary;
updateCurrent = function(current) {
	window.current = current;
	rebuildPlayIcon();
};
playingChanged = function(playing) {
	window.playing = playing;
	rebuildPlayIcon();
};
aliveChanged = function(alive) {
	window.alive = alive;
	rebuildPlayIcon();
};
uploadCompleted = updateJob;
downloadProgress = updateJob;
volumeUpdated = setVolume;