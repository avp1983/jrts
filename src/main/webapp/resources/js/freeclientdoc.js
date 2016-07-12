function onClickFreeDocAdminCreate(){
    loadForm('FREECLIENTDOC', 'NEW', 'freeclientdoctab');
}
function onClickFreeDocAdminForSent(){
    loadScroller('FREECLIENTDOC', 'NEW', 'freeclientdoctab');
}
function onClickFreeDocAdminInProcess(){
    loadScroller('FREECLIENTDOC', 'INPROCESS', 'freeclientdoctab');
}
function onClickFreeDocAdminRejected(){
    loadScroller('FREECLIENTDOC', 'REJECTED0GTHEN0', 'freeclientdoctab');
}
function onClickFreeDocAdminCompleted(){
    loadScroller('FREECLIENTDOC', 'COMPLETED', 'freeclientdoctab');
}
function onClickFreeDocAdminAll(){
    loadScroller('FREECLIENTDOC', 'ALL', 'freeclientdoctab');
}