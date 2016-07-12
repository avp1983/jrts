"use strict";
$(function () {
    $(".ui-dashboard_widget-important tr:even td").addClass('ui-panelgrid-even');
    $(".elem_report_ready").closest('tr').on('click', function(){resetFinishedReports(); navigateScrollFilter('ExprRepRequest',null,'important');});
    $(".elem_new_cert_req").closest('tr').on('click', function(){resetNewCertRequest(); navigateScrollFilter('ADMINCRYPTO', null, 'important')});
    $(".elem_msg_from_admin").closest('tr').on('click', function(){resetNewMsgFromAdmin(); navigateScrollFilter('FREECLIENTDOC_INBOX', null, 'important')});
    $(".elem_msg_from_agent").closest('tr').on('click', function(){resetNewMsgFromAgents(); navigateScrollFilter('FREEAGEXDOC_INBOX', null, 'important')});
    $(".elem_news").closest('tr').on('click', function(){resetNewsAddedToday(); navigateScroller('NEWS','DEFAULT','important')});
    $(".elem_current_limit").closest('tr').on('click', function(){/*resetCurrentLimit();*/ $('.elem_current_limit').removeClass('ui-widget-counter-updated'); updateCurrentLimit()});
    $(".elem_bonus_points").closest('tr').on('click', function(){resetLoyalityPoints(); updateBonusPoints()});
});
