"use strict";
function UpdateTransferCountStatistic(){
    $.ajax({
        type: "GET",
        url: "../s/bsi.dll?T=RT_2UMTPayTransferOpen.GetTransferCountWidgetData&SID="+top.SID,
        cache: false
    })
    .success(function (data) {
        var resXML = $.parseXML(data);
        if (resXML){
            $('.elem_transfercount_blocked').text(resXML.selectSingleNode("//BLOCKED_ALL").text);
            $('.elem_transfercount_return').text(resXML.selectSingleNode("//BLOCKEDCANCEL").text);
            $('.elem_transfercount_address').text(resXML.selectSingleNode("//ADDRESS").text);
            $('.elem_transfercount_return_paylegal').text(resXML.selectSingleNode("//BLOCKEDCANCELPL").text);
        }
    });
}
$(function () {
   UpdateTransferCountStatistic();
   $(".ui-dashboard_widget-transfer_count tr:odd td").addClass('ui-panelgrid-odd');
   $(".elem_transfercount_blocked").closest('tr').bind('click', function(){navigateScrollFilter("UMTPAYTRANSFEROPEN","&menuclass=lock","transfercountwidget")});
   $(".elem_transfercount_return").closest('tr').bind('click', function(){navigateScrollFilter("UMTPAYTRANSFEROPEN","&menuclass=to_return","transfercountwidget")});
   $(".elem_transfercount_address").closest('tr').bind('click', function(){navigateScrollFilter("UMTPAYTRANSFEROPEN","&menuclass=address_transfer","transfercountwidget");});
   $(".elem_transfercount_return_paylegal").closest('tr').bind('click', function(){navigateScrollFilter("UMTPAYLEGAL","&menuclass=to_return","transfercountwidget")});
});
