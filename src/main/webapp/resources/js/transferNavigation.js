"use strict";
function getDocInfoFromServer(scheme, docIDR) {
    return $.ajax({
        type: "GET",
        url: "../s/bsi.dll?T=RT_2" + scheme + ".GetTransferInfoForAction&SID=" + top.SID + "&IDR=" + docIDR,
        cache: false,
        async: false
    }).responseText;
}

function unblockTransfer(docIDR) {
    var docInfoXML = $.parseXML(getDocInfoFromServer('UMTPAYTRANSFEROPEN', docIDR));
    var docStatus = docInfoXML.selectSingleNode('//status').text;
    switch (docStatus) {
        case "17111":
            navigateBSI('RT_2UMTPayTransferOpen.UnlockEdit', 'UMTPAYTRANSFEROPEN', null, null, '&IDR=' + docIDR, null, 'paytransferopenwidget');
            break;
        case "27091":
        case "17221":
            navigateBSI('RT_2UMTPayTransferClose.unlock', 'UMTPAYTRANSFERCLOSE', (docStatus == "27091")?"UNLOCKPAY":"UNLOCKRETURN", null, '&TransferOpenIDR=' + docIDR, null, 'paytransferopenwidget');
            break;
    }
}

function undoTransferSending(docIDR) {
    $.ajax({
        type: "GET",
        url: top.scriptPath + getURL('RT_2IC.sc_STATUS', 'UMTPAYTRANSFEROPEN', 'UNDO_SENDING', null,
                '&IDRsToGoProc=' + docIDR + '&NotShowDocCount=1&WSTATUS=3712', 'paytransferopenwidget'),
        contentType: "text/xml"
    })
        .always(function (data, status, xhr) {
            top.fnOnDataResult(top.mw, 'UNDO_SENDING', xhr);
            // Блокируем перевод для выплаты
            navigateBSI('RT_2UMTPayTransferClose.lock', 'UMTPAYTRANSFERCLOSE', 'LOCKRETURN', 'BLOCKEDCANCEL', '&IDR=' + docIDR, null, 'paytransferopenwidget');
        });
}

function editTransfer(docIDR) {
    navigateBSI('RT_2UMTPayTransferOpen.lockEdit', 'UMTPAYTRANSFEROPEN', 'VIEWEDIT', null, '&IDR=' + docIDR, null, 'paytransferopenwidget');
}

function returnTransfer(docIDR) {
    navigateBSI('RT_2UMTPayTransferClose.lock', 'UMTPAYTRANSFERCLOSE', 'LOCKRETURN', null, '&IDR=' + docIDR, null, 'paytransferopenwidget');
}
function copyTransfer(docIDR) {
    navigateFormMW('UMTPAYTRANSFEROPEN', 'ADD', docIDR, null, 'paytransferopenwidget');
}

//noinspection JSUnusedLocalSymbols
function copyPayLegalComplete(xhr, status, args) {
    if (args.scheme == 'UMTPAYLEGAL') {
        navigateFormScrollFilter('UMTPAYLEGAL', 'FORM', args.newpk);
    }
}

function openSupposedTransferActionForm(docIDR) {
    var docInfoXML = $.parseXML(getDocInfoFromServer('UMTPAYTRANSFEROPEN', docIDR));
    var docStatus = docInfoXML.selectSingleNode('//status').text;
    var isBlockedByCurrentCustomer = (docInfoXML.selectSingleNode('//isBlockedByCurrentCustomer').text == '1');
    switch (docStatus) {
        case "32000":
            navigateForm('UMTPAYTRANSFEROPEN', 'NEW_FROM_DRAFT', docIDR, null, 'paytransferopenwidget');
            break;
        case "32101":
        case "1001":
        case "17007":
            navigateForm('UMTPAYTRANSFEROPEN', 'VIEWNEW', docIDR, '&SHOW_AS_VIEW=1', 'paytransferopenwidget');
            break;
        case "17111":
            if (isBlockedByCurrentCustomer) {
                editTransfer(docIDR);
                break;
            } else {
                navigateView('UMTPAYTRANSFEROPEN', 'VIEW', docIDR, null, 'paytransferopenwidget');
            }
            break;
        case "27091":
            if (isBlockedByCurrentCustomer) {
                navigateBSI('RT_2UMTPayTransferClose.lock', 'UMTPAYTRANSFERCLOSE', 'VIEWEDIT', null, '&IDR=' + docIDR, null, 'paytransferopenwidget');
                break;
            } else {
                navigateView('UMTPAYTRANSFEROPEN', 'VIEW', docIDR, null, 'paytransferopenwidget');
            }
            break;
        case "17221":
            if (isBlockedByCurrentCustomer) {
                returnTransfer(docIDR);
                break;
            } else {
                navigateView('UMTPAYTRANSFEROPEN', 'VIEW', docIDR, null, 'paytransferopenwidget');
            }
            break;
        default :
            navigateView('UMTPAYTRANSFEROPEN', 'VIEW', docIDR, null, 'paytransferopenwidget');
    }
}

function openSupposedPayoutActionForm(docIDR) {
    var docInfoXML = $.parseXML(getDocInfoFromServer('UMTPAYTRANSFERCLOSE', docIDR));
    var isBlockedByCurrentCustomer = (docInfoXML.selectSingleNode('//isBlockedByCurrentCustomer').text == '1');
    var openIDR = docInfoXML.selectSingleNode('//openIDR').text;
    var docStatus = docInfoXML.selectSingleNode('//status').text;
    var openStatus = docInfoXML.selectSingleNode('//openStatus').text;
    switch (openStatus) {
        case "32000":
            navigateForm('UMTPAYTRANSFERCLOSE', 'NEW_FROM_DRAFT', docIDR, null, 'paytransferclosewidget');
            break;
        case "27091":
            if (isBlockedByCurrentCustomer) {
                if (docStatus == "32000"){
                    navigateForm('UMTPAYTRANSFERCLOSE', 'NEW_FROM_DRAFT', docIDR, null, 'paytransferclosewidget');
                }
                else if (docStatus == "32101"){
                    navigateForm('UMTPAYTRANSFERCLOSE', 'LOCKPAY', docIDR, null, 'paytransferclosewidget');
                }
                else if (docStatus == "17008"){
                    navigateForm('UMTPAYTRANSFERCLOSE', 'VIEW_TOPAY', docIDR, '&SHOW_AS_VIEW=1', 'paytransferclosewidget');
                }
                break;
            } else {
                navigateView('UMTPAYTRANSFERCLOSE', 'VIEW', docIDR, null, 'paytransferclosewidget');
            }
            break;
        case "17221":
            if (isBlockedByCurrentCustomer) {
                returnTransfer(openIDR);
                break;
            } else {
                navigateView('UMTPAYTRANSFERCLOSE', 'VIEW', docIDR, null, 'paytransferclosewidget');
            }
            break;
        default :
            navigateView('UMTPAYTRANSFERCLOSE', 'VIEW', docIDR, null, 'paytransferclosewidget');
    }
}

function openSupposedPayLegalActionForm(docIDR) {
    var docInfoXML = $.parseXML(getDocInfoFromServer('UMTPAYLEGAL', docIDR));
    var docStatus = docInfoXML.selectSingleNode('//status').text;
    switch (docStatus) {
        case "32000":
        case "32101":
        case "1001":
        case "7301":
        case "7303":
        case "17007":
            navigateScrollFilter('UMTPAYLEGAL', '&IDR=' + docIDR, 'paylegalwidget');
            break;
        default :
            navigateView('UMTPAYLEGAL', 'VIEW', docIDR, null, 'paylegalwidget');
    }
}
