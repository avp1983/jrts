"use strict";
$(function () {
    var waitPFInterval = setInterval(function () {
        if (!PF('lastTransferTable')) {
            return;
        }
        clearInterval(waitPFInterval);
        addRowClickHandlers(PF('lastTransferTable'));
    }, 300)
});

function addRowClickHandlers(lastTransferTable) {
    lastTransferTable.jq.find("tbody .ui-datatable-last_transfer-row").each(function () {
        $(this).bind('click', function () {

            if ($(this).find(".docType").text() == 'PayLegal') {
                openSupposedPayLegalActionForm($(this).find(".IDR").text());
            } else {
                openSupposedTransferActionForm($(this).find(".IDR").text());
            }
        });
    });
}
