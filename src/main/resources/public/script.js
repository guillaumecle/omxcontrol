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
    });
}
function add(elem) {
    console.log(elem.previousSibling.previousSibling.value);
}