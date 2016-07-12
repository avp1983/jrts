"use strict";
$(function () {
    $(".ui-dashboard_widget-addcommissions tr:odd td").addClass('ui-panelgrid-odd');
    $(".elem_not_accepted").closest('tr').on('click', function () {
        resetAddCommissions();
        navigateScroller('UMTCommExt','REJECTED','addCommissionsWidget');
    });
    $(".elem_addcomm_active").closest('tr').on('click', function () {
        resetAddCommissions();
        navigateScroller('UMTCommExt','ACTIVE','addCommissionsWidget');
    });
});