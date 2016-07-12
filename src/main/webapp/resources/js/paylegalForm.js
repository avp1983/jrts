var TAB_KEY = 9;

"use strict";
function onKeyDownFirstComponentOnMainDoc() {
    if (event.keyCode == TAB_KEY && event.shiftKey) {
        PF('mainDocPanel').hide();
        event.returnValue = false;
    }
}
"use strict";
function onKeyDownLastComponentOnMainDoc() {
    if (event.keyCode == TAB_KEY && !event.shiftKey) {
        PF('mainDocPanel').hide();
    }
}

"use strict";
function onKeyDownFirstComponentOnOtherDoc() {
    if (event.keyCode == TAB_KEY && event.shiftKey) {
        PF('otherDocPanel').hide();
        event.returnValue = false;
    }
}
"use strict";
function onKeyDownLastComponentOnOtherDoc() {
    if (event.keyCode == TAB_KEY && !event.shiftKey) {
        PF('otherDocPanel').hide();
    }
}
"use strict";
function printRequestForm() {
    var printDiv = $(".elem-print");
    $.ajaxSetup({
        cache: false
    });
    printDiv.load(top.Top.scriptPath + '?SID=' + top.Top.SID + '&T=RT_2UMTPayLegal.GetPrintForm&ID=' + $(".docID").val(),
        function (data, result) {
            if (result == "success") {
                printDiv.jqprint();
            }
            printDiv.text("");
        });
    $(".printRequest").click();
}

var bssDoc = new BSSTransfer();

function signDocument() {
    var docIDR = $(".docIDR").val();
    var res = bssDoc.signDocument("UmtPayLegal", docIDR, 3050);
    processSignDocument([
        {name: 'resCode', value: res.resCode},
        {name: 'errmessage', value: res.errmessage},
        {name: 'message', value: res.message}
    ]);

    if ((res.resCode === '7') && (res.errmessage === '')) {
        var idrArr = docIDR.split('|');
        if (idrArr.length > 0) {
            ExportTransfer(idrArr[0], true);
        }
    }
}

function ExportTransfer(custid, autoexport) {
    try {
        var xLR = LoadLResXML('umtpaylegal', top.Top.LanguageID.toLowerCase());
        top.df = {"SCHEMENAME": {"value": "UMTPAYLEGAL"}};
        top.df = {"SID": {"value": top.Top.SID}, "ID": {"value": $(".docID").val()}, "SCHEMENAME": {"value": "UMTPAYLEGAL"}, "CUSTID": {"value": custid}};
        top.Top.ExportToFile(window, $(".docIDR").val(), $(".checkNumber").val(), 3, autoexport, autoexport, xLR.selectSingleNode('//FileExport1').text, xLR.selectSingleNode('//FileExport2').text, xLR.selectSingleNode('//FileExport3').text);
    } catch (e) {
        if (!autoexport) {
            PF('growl').renderMessage({"summary": "Ошибка выгрузки документа.",
                "detail": "",
                "severity": "error"});
        }
    }
}


function addInputBindings() {
    var rx = /INPUT|SELECT|TEXTAREA/i;
    $(document).bind("keydown keypress", function (e) {
        if (e.which == 8) { // 8 == backspace
            if (!rx.test(e.target.tagName) || e.target.disabled || e.target.readOnly) {
                e.preventDefault();
            }
        }
        if (rx.test(e.target.tagName)) {
            $(".forceSave").val("false");
            $(".isFormChanged").val("true");

            if (e.ctrlKey && e.keyCode == 13) {
                onClickButtons();
            }
        }
    });
}

function reposOverlayWithCollision (widget, collision) {
    widget.align = function() {
        var fixedPosition = this.jq.css('position') == 'fixed',
            win = $(window),
            positionOffset = fixedPosition ? '-' + win.scrollLeft() + ' -' + win.scrollTop() : null;

        this.jq.css({'left':'', 'top':'', 'z-index': ++PrimeFaces.zindex})
            .position({
                my: this.cfg.my
                ,at: this.cfg.at
                ,of: document.getElementById(this.cfg.target)
                ,offset: positionOffset
                ,collision: collision
            });
    };
    widget.align();
}

function setClickOverlayPanel(panelName, parentInputName) {
    var overlaypanel = PF(panelName);

    reposOverlayWithCollision(overlaypanel, 'none');

    overlaypanel.jq.children().on('click',function(e){
        e = e || window.event;

        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    });

    // скрытие других overlaypanel
    var panels = $('.big-overlay-panel').not(overlaypanel.jq);
    for (var i = 0; i < panels.length; i++) {
        for (var widgetName in PrimeFaces.widgets) {
            if (PrimeFaces.widgets[widgetName] instanceof PrimeFaces.widget.OverlayPanel) {
                if ((PrimeFaces.widgets[widgetName].jq.attr('id') === panels.eq(i).attr('id'))
                    &&(PrimeFaces.widgets[widgetName].jq.attr('id') != overlaypanel.jq.attr('id'))
                    &&(PrimeFaces.widgets[widgetName].jq.hasClass('ui-overlay-visible'))) {
                    PrimeFaces.widgets[widgetName].hide();
                }
            }
        }
    }

    var targettextarea = PF(parentInputName);
    overlaypanel.jq.width(targettextarea.jq.width());

    var w = overlaypanel.jq.find('.panel-doc').width();
    overlaypanel.jq.find('.ui-overlaypanel-content').width(w);

    overlaypanel.jq.off('click');
    overlaypanel.jq.on('click', function() {
        targettextarea.jq.click();
    });
}

$(function () {
    if (isStandaloneWindow()) {
        window.resizeTo(1100, 700);
    }
    setSenderAccountVisible();
    addInputBindings();

    $(window).unload(function () {
        if ($(".isFormChanged").val() == "true" || $(".wasInstanceSave").val() == "true") {
            addDocToTaskBarWithPreSave();
            addDocToTaskBoardByIDR('UMTPAYLEGAL', PF('IDR').jq.val());
        }
    });
    setTimeout(function () {
        $(".elem-amount .ui-inputtext").focus();
    }, 250);
    addResizeToDialogMessages();
    $("body").css("height", $(window).height());

});

function addResizeToDialogMessages() {
    $(".dialog-messages").resize(function () {
        $(".dialog-messages .ui-dialog-content").css("height",
                $(".dialog-messages").height() - $(".dialog-messages .ui-dialog-titlebar").height() - 15);
    });
}

function processErrorMessages() {
    if ($('.elem-messages UL').length > 0) {
        var messageDialog = PF('messagesDialog');
        var messagePanel = messageDialog.jq.find(".ui-messages");
        var height = Math.max(150, messagePanel.height() + 60);
        var width = Math.max(500, messagePanel.width() + 35);
        messageDialog.jq.css({
            "width": width,
            "height": height,
            "min-width": width,
            "min-height": height
        });
        messageDialog.jq.find(".ui-dialog-content").css({
            "min-width": width
        });
        PF('messagesDialog').show();
    }
}

function onBeforDocAction() {
    $(".panel-buttons button").attr('disabled', 'disabled');
    PF('messagesDialog').hide();
    PF('statusDialog').show();
}

function onCompleteDocAction() {
    $(".panel-buttons button").removeAttr('disabled');
    addInputBindings();
    PF('statusDialog').hide();

    $(".isFormChanged").val("false");
    $(".wasInstanceSave").val("false");
    processErrorMessages();
}

function isStandaloneWindow() {
    return top.Top != top;
}

function focusSaveButton() {
    $(".elem-button_save").focus();
}
function setFocusAfterSave() {
    setTimeout(function () {
        $("input.elem-add_param:enabled, .elem-add_param input:enabled, .elem-button_save, .button-confirm").first().focus()
    }, 250);
}
function setSenderAccountVisible() {
    var accounElem = $(".elem-use_sender_account");
    if (accounElem.length == 0) {
        return;
    }
    var isVisibleAccount = accounElem.get(0).checked;
    if (isVisibleAccount) {
        $(".panel-sender_account_input").show();
        $(".elem-sender_account").focus();
    } else {
        $(".panel-sender_account_input").hide();
        $(".elem-sender_account").val("");
    }
}

function onClickButtons() {
    if (PF('btnConfirm')) {
        PF('btnConfirm').jq.click();
    } else {
        if (PF('btnSave')) {
            PF('btnSave').jq.click();
        }
    }
}

function instanceSave() {
    if (!window.instanceSaveCommand) {
        return;
    }

    var formChangedElem = $(".isFormChanged");
    if (formChangedElem.val() == "true") {
        instanceSaveCommand([
            {name: 'INSTANCE_SAVE_FLAG', value: 1}
        ]);
        formChangedElem.val("false");
        $(".wasInstanceSave").val("true");
    }
}

function toogleTooltip() {
    setTimeout(function () {
        var amount = +$(".elem-amount .ui-inputtext").val().replace(/[^\.\d]/g, "");
        var maxAmount = +$(".providerMaxAmount").text();
        if (amount < +$(".providerMinAmount").text() || (maxAmount > 0 && amount > maxAmount)) {
            PF('widgetTooltipAmount').show();
        }
        else {
            PF('widgetTooltipAmount').hide();
        }
    }, 100);
}

function onCompleteSaveForPrint(args) {
    if (!args.validationFailed) {
        printRequestForm();
    }
    onCompleteDocAction();
}
function onCompleteSaveForExport(args, agentID) {
    onCompleteDocAction();
    if (!args.validationFailed) {
        ExportTransfer(agentID, false);
    }
}