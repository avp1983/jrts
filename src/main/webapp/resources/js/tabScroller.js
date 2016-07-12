function writeWaitMessage(win){
    win.document.open();
    win.document.writeln("<DIV id=WaitMes style='color: #ff6300;'>"+top.LRS3+"</DIV>");
    win.document.close();
}

function setCorrectHeight() {
    var mainFormHeight = $(window).height();
    $("[id$='mainForm']").height(mainFormHeight);
    $("#id_scroller_content").height(mainFormHeight - PF('scrollerfilter').jq.height() - 12);
}
$(function() {
    var startMenuClass = $(".StartMenuClass").text();
    if (startMenuClass != "") {
        $(".ui-menuitem-" + startMenuClass).click();
    } else {
        //$(".ui-tabmenu-nav li:first-child a").click();
        $(".ui-tabmenu-nav .ui-state-active a").click();
    }
    setCorrectHeight();
    $(window).resize(function(){
        setCorrectHeight();
    });
    var mainFrameContentWin = $("#id_scroller_content").get(0).contentWindow;
    top.tb.mw = mainFrameContentWin;
    top.mw = mainFrameContentWin;
});
$( window ).unload(function() {
    var mainFrameContentWin = top.getFrame(top, top.ccSITE+'_MAINW');
    top.tb.mw = mainFrameContentWin;
    top.mw = mainFrameContentWin;
});

function loadURL(url) {
    var contentWin = $('#id_scroller_content');
    writeWaitMessage(contentWin.get(0).contentWindow);
    contentWin.attr('src', url);
}

function loadScroller(schemeName, filterIdent, widgetName) {
    loadURL(getURL(top.MainBllName + '.SC', schemeName, null, filterIdent, null, widgetName));
}

function loadForm(schemeName, formAction, widgetName, additional) {
    loadURL(getURL(top.MainBllName + '.form', schemeName, formAction, additional, null, widgetName));
}

function loadByTask(func, schemeName, formAction, additional, widgetName) {
    loadURL(getURL(func, schemeName, formAction, null, additional, widgetName));
}