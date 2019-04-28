// slideshare overlay
var $overlay = $('<div id="overlay"></div>');
var $iframe = $('<iframe height="500" style="width: 50%; display: inline;"></iframe>');

// append iframe to overaly
$overlay.append($iframe);

// append overlay to body
$('body').append($overlay);

// slideshare slides - capture the click event on a link to the slide
$('a.view-slide').click(function (event) {
    event.preventDefault();
    var id = $(this).attr('href');
    var src = 'https://tawk.to/chat/5cb4f846c1fe2560f3fefa73/default';
    // update overlay with iframe
    $iframe.attr('src', src);
    // show overlay
    $overlay.show();
});

// when overlay is clicked
$overlay.click(function () {
    // hide overlay
    $overlay.hide();
    $iframe.attr('src', '');
});