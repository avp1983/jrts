function addDocToTaskBoardByIDR(schemename, IDR){
    if ((schemename == "") || (schemename == "undefined")){
        return;
    }
    if ((IDR == "") || (IDR == "undefined")){
        return;
    }
    top.Top.taskbar.addDocToBoard([{name:'IDR', value:IDR}, {name:"schemename", value:schemename}]);
    setTimeout(function(){
        $("html").scrollTop(0);
    },100);
}

function LoadLResXML(scheme, lang){
    var oXML = new ActiveXObject(top.Top.XMLDOM_ID);
    oXML.validateOnParse = false;
    oXML.preserveWhiteSpace = true;
    oXML.async = false;
    oXML.load('../scheme/' + scheme + '/' + scheme + '_' + lang + '.xml');
    return oXML;
}
