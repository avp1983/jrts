function writeWaitMessage(win){
    win.document.open();
    win.document.writeln("<DIV id=WaitMes style='color: #ff6300;'>"+top.LRS3+"</DIV>");
    win.document.close();
}

function getURL(taskName, schemeName, xAction, filterIdent, additional, widgetName) {
    if (typeof top.AddToHistory == 'function')
        top.AddToHistory(taskName, schemeName, xAction, filterIdent, widgetName);
    return '?T=' + taskName + '&nvgt=1' +
        ((top.SID == '') ? '' : '&SID=' + top.SID) +
        ((schemeName == null) ? '' : '&SCHEMENAME=' + schemeName) +
        ((xAction == null) ? '' : '&XACTION=' + xAction) +
        ((filterIdent == null) ? '' : '&FILTERIDENT=' + filterIdent) +
        ((additional == null) ? '' : additional) +
        '&TMS=' + top.fnRnd() +
        ((top.bDemo) ? ('&L=' + top.LanguageID) : '');
}

function navigate(url, target) {
    "use strict";
    if (top.FDataChanged(top.mw)) return;
    top.getFrame(top, top.ccSITE + '_MAINW').window.open(url, ((target != null) ? target : (top.ccSITE + '_MAINW')));
}

function navigateBSI(taskName, schemeName, xAction, filterIdent, additional, target, widgetName) {
    "use strict";
    var URL = getURL(taskName, schemeName, xAction, filterIdent, additional, widgetName);
    navigate(top.scriptPath + URL, target);
}

function returnMainWindowFrame() {
    var mainFrameContentWin = top.getFrame(top, top.ccSITE + '_MAINW');
    top.tb.mw = mainFrameContentWin;
    top.mw = mainFrameContentWin;
}

//noinspection JSUnusedGlobalSymbols
function navigateScrollFilter(schemeName, params, widgetName) {
    returnMainWindowFrame();
    navigateBSI('RT_2IC.ScrollerFilter', schemeName, null, null, params, null, widgetName);
}

function navigateScroller(schemeName, filterIdent, widgetName) {
    "use strict";
    navigateBSI(top.MainBllName + '.SC', schemeName, null, filterIdent, null, null, widgetName);
}

function navigateFormMW(schemeName, formAction, IDR, additional, widgetName) {
    "use strict";
    returnMainWindowFrame();
    navigateBSI(top.MainBllName + '.form', schemeName, formAction, null, (IDR ? '&IDR=' + IDR : "") + (additional ? additional : ''), null, widgetName);
}

function navigateForm(schemeName, formAction, IDR, additional, widgetName) {
    "use strict";
    navigateBSI(top.MainBllName + '.form', schemeName, formAction, null, (IDR ? '&IDR=' + IDR : "") + (additional ? additional : ''), null, widgetName);
}

function navigateFormScrollFilter(schemeName, formAction, IDR, additional, widgetName) {
    var mainFrameContentWin = top.getFrame(top, top.ccSITE + '_MAINW');
    writeWaitMessage(top.mw);
    if (mainFrameContentWin.loadForm) {
        mainFrameContentWin.loadForm(schemeName, formAction, widgetName, (IDR ? '&IDR=' + IDR : "") + (additional ? additional : ''));
    }
    else {
        navigateForm(schemeName, formAction, IDR, additional, widgetName)
    }
}
function navigateView(schemeName, formAction, IDR, additional, widgetName, width, height) {
    if (!height){
        height = 450;
    }
    if (!width){
        width = 450;
    }
    var URL = getURL(top.MainBllName + '.view', schemeName, formAction, null, '&IDR=' + IDR + (additional ? additional : ''), widgetName);
    var newwin = top.getFrameSet(top.mw).window.open(URL, "_blank", "scrollbars=yes,resizable=yes,toolbar=no,menubar=no,status=no" +
        ",height=" + height + ",width=" + width);
    var thisWin = top;
    newwin.attachEvent('onload', function(){
        newwin.Top = thisWin.window;
        newwin.jsOwner = thisWin.mw;
        newwin.IsVIEW = true;
    }, false);
}

function navigateLogOut() {
    top.SID = '';
    navigateBSI('RT_1Loader.load', null, null, null, null, '_top');
}

$(function () {
    top.nv.done = '1';
});
