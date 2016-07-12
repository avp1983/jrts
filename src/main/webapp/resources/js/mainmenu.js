var idleMonitor;
$(document).ready(function () {
    $('li.ui-menu-parent').width('22px').css('margin', '-5px');
    $('a.ui-menuitem-link.ui-corner-all').attr('href', '#');
    $('.ui-icon-triangle-1-e').hide();

    changeWidth(window.screen.availWidth - 150);
    var waitPFTimer = setInterval(function () {
        if (!PF) {
            return;
        }
        clearInterval(waitPFTimer);
        if ($('.isshowmenututorial').text() == "true" && typeof(PF('mainMenu')) != 'undefined') {
        showMegaMenuTutorial();
        }
    }, 300);
    createGlobalIdleMonitor();
});

var closeSessionAfterDialogTimer;
var autoLogoutTimeout = parseInt($(".ui-elem-setting-clientAutoLogoutTimeout").text());
function createGlobalIdleMonitor() {
    var autoLogoutFormTimeout = 5;
    idleMonitor = new GlobalIdleMonitor(1000 * 60 * autoLogoutTimeout, function () {
        PF('closeSessionDialog').show();
        PF('continueSessionBtn').jq.focus();
        closeSessionAfterDialogTimer = setTimeout(logOut, 1000 * 60 * autoLogoutFormTimeout);
    });
    idleMonitor.start();
}

function continueSession() {
    clearTimeout(closeSessionAfterDialogTimer);
    PF('closeSessionDialog').hide();
    $('.ui-widget-overlay').hide();
    idleMonitor.reset();
    idleMonitor.start();
}

function logOut() {
    window.onunload = null;
    window.onbeforeunload = null;
    $.ajax({
        type: "GET",
        url: "../s/bsi.dll?T=RT_1Entry.CloseSess&SID=" + top.SID,
        cache: false
    });
    navigateLogOut();
}

function showMegaMenuTutorial() {
    var growl = PF('messagesGrowl');
    var mainMenu = PF('mainMenu');
    var spot = PF('spot');

    setTimeout(function () {
        spot.show();
        $('.ui-widget-overlay').show();
        growl.jq.addClass('ui-state-growl_for_mainmenu');
        growl.show([
            {summary: MAIN_MENU_NEW_ELEM, severity: 'info', detail: MAIN_MENU_FIRST_ENTER_HELP}
        ]);
        growl.jq.find('div.ui-growl-icon-close:first').show();
        growl.jq.on('mouseout', function () {
            $(this).find('div.ui-growl-icon-close:first').show();
        });
        setTimeout(function () {
            growl.jq.fadeOut("slow", function () {
                if (mainMenu.jq.find('.ui-menu-parent .ui-menu-list').css("display") == "none") {
                    mainMenu.jq.find('.ui-submenu-link').get(0).click();
                }
                growl.removeAll();
                growl.jq.removeClass('ui-state-growl_for_mainmenu');
                $('.ui-widget-overlay').hide();
                spot.hide();
            });
        }, 8000)
    }, 1000);
}

//noinspection JSUnusedGlobalSymbols
function navigateReport(repId) {
    navigateBSI('core.reportNew', 'report' + repId, null, null, '&amp;nvgt=1', null, 'mainmenu');
}

function changeWidth(menuWidth) {
    var $columnCount = $("ul[role='menu'] table td").size();
    if (menuWidth < ($("ul[role='menu']").width())) {
        $('.ui-megamenu.ui-menu .ui-menu-child .ui-menu-list').width(menuWidth / $columnCount);
    }
}