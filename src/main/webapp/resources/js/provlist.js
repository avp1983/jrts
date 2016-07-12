$(function () {
    updateGrowl();
    setFavouritesHeader();
});

function onKeyUpSearchButton() {
    if (event.keyCode == 13) {
        PF('searchButton').jq.click();
        return false;
    }
    if ((PF('searchInput').jq.val().length > 2) || ((PF('searchInput').jq.val().length == 0) && (window.oldSeachInputValue))) {
        if (window.searchTimerID) {
            clearTimeout(window.searchTimerID);
        }
        window.searchTimerID = setTimeout("PF('searchButton').jq.click();", 1500);
    }
}

function onKeyDownSearchButton() {
}

function hideEmptyComponents(compSelector, checkSelector) {
    var $widgets = $(compSelector);
    for (i = 0; i < $widgets.length; i++) {
        var $table = $widgets.eq(i).find(checkSelector);
        $widgets.eq(i).find(".widgetFAV");

        if ((!$table.get(0)) && (!$widgets.eq(i).find(".widgetFAV").get(0))) {
            $widgets.eq(i).hide();
        }
    }
}

function hideEmptyCategories() {
    hideEmptyComponents(".ui-dashboard .ui-panel", ".widgetWPC");
    hideEmptyComponents(".ui-dashboard .ui-dashboard-column", ".widgetWPC");
}

function searchComplete() {
    window.oldSeachInputValue = PF('searchInput').jq.val();
    hideEmptyCategories();
}

function setFavouritesHeader() {
    if (!PF('favourites')) {
        setTimeout(setFavouritesHeader, 200);
        return;
    }

    PF('favourites').jq.find('.ui-panel-titlebar').addClass('ui-state-active');
}
