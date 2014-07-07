var theOtherBar;
function toggleActionBar(elem) {
	if (typeof theOtherBar === 'undefined') {
		theOtherBar = {
			uploadBar: youtubeBar,
			youtubeBar: uploadBar
		};
	}
	if (elem.className == 'enLarge') {
		elem.classList.remove('enLarge');
		document.body.classList.remove('enLarge');
	} else {
		elem.classList.add('enLarge');
		theOtherBar[elem.id].classList.remove('enLarge');
		document.body.classList.add('enLarge');
}
}