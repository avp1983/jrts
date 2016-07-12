var initTaskbarFrameWidth;
function setTaskbarFrameWidth(widthVal) {
    var generalFrame = top.getFrameSet(window).document.all.FG;
    var frameCols = generalFrame.cols.split(",");
    frameCols[frameCols.length - 1] = widthVal + 'px';
    generalFrame.cols = frameCols.concat();
}
function addTaskbarBehaviour() {
    if (!PF('pageLayout')) {
        setTimeout(addTaskbarBehaviour, 200);
        return;
    }
    var eastPanel = PF('pageLayout').layout.east;
    eastPanel.options.onclose_end = function () {
        setTaskbarFrameWidth(PF('pageLayout').layout.east.resizer.width());
    };
    eastPanel.options.onopen_start = function () {
        setTaskbarFrameWidth(initTaskbarFrameWidth);
        PF('pageLayout').layout.state.container.innerWidth = initTaskbarFrameWidth;
        $("#eastLayout").css("right", 0);
    };
}

$(function () {
    $.ajaxSetup ({
        cache: true
    });
    $(".panel-support .elem-tech_support_info").html(top.techSupportInfo);
    var generalFrameCols = top.getFrameSet(window).document.all.FG.cols.split(",");
    initTaskbarFrameWidth = generalFrameCols[generalFrameCols.length - 1];
    addTaskbarBehaviour();
});
