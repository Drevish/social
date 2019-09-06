$(document).ready(function () {
    $(".settings-link").click(function (e) {
        var row = $(this).parent();
        row.toggleClass("active");

        if ($(this).text() === 'Change') {
            $(this).text('Cancel');
        } else {
            $(this).text('Change');
        }

        $(row.find(".default-value")[0]).toggle();
        $(row.find(".active-value")[0]).toggle();
        // hide default and show active value
    });
});