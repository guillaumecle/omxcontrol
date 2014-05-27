function buildPlaylist(playlist) {
	var container = jQuery('#playlist');
	container.empty();
	for (var i = 0; i < playlist.list.length; i++) {
		var track = playlist.list[i];
		container.append($('<li>').text(track));
	}
}
function addFromLib(elem) {
    jQuery.ajax({
        url: '/ajax/add/' + encodeURIComponent(elem.textContent),
        success: function(data) {
			buildPlaylist(data);
        },
		dataType: 'json'
    });
}
function pause() {
    jQuery.ajax({
        url: 'ajax/pause'
    })
}
function add(elem) {
    console.log(elem.previousSibling.previousSibling.value);
}