function onClickPayLegalCreate() {
    var $IDR = $(".IDR");
    var $providerid = $(".providerid");
    var params = ($IDR.text() == '' ? '' : '&IDR=' + $IDR.text()) + ($providerid.text() == '' ? '' : '&providerid=' + $providerid.text());
    loadForm('UMTPAYLEGAL', 'NEW', 'paylegaltab', params);
    $IDR.text("");
    $providerid.text("");
}
function onClickPayLegalWaitForSend() {
    loadScroller('UMTPAYLEGAL', 'NEW', 'paylegaltab');
}
function onClickPayLegalSent() {
    loadScroller('UMTPAYLEGAL', 'SENT', 'paylegaltab');
}
function onClickPayLegalToReturn() {
    loadScroller('UMTPAYLEGAL', 'BLOCKEDCANCEL', 'paylegaltab');
}