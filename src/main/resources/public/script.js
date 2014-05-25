function addFromLib(elem) {
    jQuery.ajax({
        url: '/ajax/add/' + encodeURIComponent(elem.textContent)
    });
}
function add(elem) {
    console.log(elem.previousSibling.previousSibling.value);
}