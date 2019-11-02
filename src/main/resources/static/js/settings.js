$(document).ready(function () {
    let opened = null;


    $(".settings-link").click(function () {
        if (opened === this) {
            // just closing the only opened row
            opened = null;
        } else {
            if (opened) {
                // another row was opened, close it
                $(opened).trigger("click");
            }

            // storing opened row
            opened = this;
        }

        var row = $(this).parent();
        const isClosing = row.hasClass("active");
        if (!isClosing) {
            setTimeout(() => row.find("input.to-focus").focus(), 0);
        }

        row.toggleClass("active");

        if ($(this).text() === 'Change') {
            $(this).text('Cancel');
        } else {
            $(this).text('Change');
        }

        $(row.find(".default-value")[0]).toggle();
        $(row.find(".active-value")[0]).toggle();
    });
});