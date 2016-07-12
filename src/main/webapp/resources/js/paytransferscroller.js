function onClickTcn_search(){
    loadByTask('RT_2UMTPAYTRANSFERCLOSE.setPropsSearch','UMTPAYTRANSFEROPEN', 'NOTPAY',null,'paytransferopentab');
}
function onClickLast_transfer(){
    loadByTask('RT_2UMTPAYTRANSFEROPEN.lastTransfer',' LAST',null,null,'paytransferopentab');
}
function onClickWait_send(){
    loadScroller('UMTPAYTRANSFEROPEN','NEW','paytransferopentab');
}
function onClickWait_pay(){
    loadScroller('UMTPAYTRANSFEROPEN','SENT','paytransferopentab');
}
function onClickTemplate(){
    loadScroller('UMTPAYTRANSFEROPEN','TEMPLATES','paytransferopentab');
}
function onClickLock(){
    loadScroller('UMTPAYTRANSFEROPEN','BLOCKED_ALL','paytransferopentab');
}
function onClickAddress_transfer(){
    loadByTask('RT_2AddressTransfers.view','',null,null,'paytransferopentab');
}
function onClickTo_return(){
    loadScroller('UMTPAYTRANSFEROPEN','BLOCKEDCANCEL','paytransferopentab');
}
function onClickArchive(){
    loadScroller('UMTPAYTRANSFEROPEN','ARCHIVE','paytransferopentab');
}
function onClickCreateTransfer(){
    loadForm('UMTPAYTRANSFEROPEN', 'NEW', 'paytransferopentab');
}
