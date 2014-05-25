function addFromLib(elem) {
    jQuery.ajax({
        url: '/ajax/add/' + encodeURIComponent(elem.textContent),
        success: function(data) {
            console.log(data);
        }
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