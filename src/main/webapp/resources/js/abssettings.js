function onClickSendTransferSettings() {
    loadURL('../userdata/advanced.htm?TRANSFER=1&CSS=' + top.csName);
}
function onClickReceiveTransferSettings() {
    loadURL('../userdata/advanced.htm?TRANSFER=2&CSS=' + top.csName);
}
function onClickPayLegalSettings() {
    loadURL('../userdata/advanced.htm?TRANSFER=3&CSS=' + top.csName);
}