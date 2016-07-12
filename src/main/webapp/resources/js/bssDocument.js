function BSSTransfer() {
    "use strict";

    this.signDocument = function (SCHEMENAME, IDR, setStatus) {
        return processSigns(SCHEMENAME, IDR, setStatus);
    };

    var getSignsReq;

    function processSigns(SCHEMENAME, IDR, setStatus) {
        "use strict";
        var result;
        $.ajax({
            type: "POST",
            url: top.scriptPath,
            data: '<?xml version="1.0" encoding="UTF-8"?><FORM><SCHEMENAME>' + SCHEMENAME + '</SCHEMENAME><FILTERIDENT>NEW</FILTERIDENT><T>RT_2IC.sc_get_signs</T><IDR>' + IDR + '</IDR><PAGENUMBER>1</PAGENUMBER><XACTION>SIG</XACTION><XMLDATA>1</XMLDATA><SGCFG>=</SGCFG><NOS>1</NOS><FTMP>0</FTMP></FORM>',
            contentType: "text/xml; charset=UTF-8",
            headers: { 'Rts-Request': 'SID=' + top.SID + '&T=RT_2IC.sc_get_signs'},
            async: false
        })
            .always(function (data, status, xhr) {
                getSignsReq = xhr;
                result = setSigns(SCHEMENAME, setStatus, xhr);
            });
        return result;
    }

    function getResult(data, xhr){
        var dataXML = $.parseXML(data);
        var errMsg = dataXML.selectSingleNode("//errmsg");
        var reply = dataXML.selectSingleNode("//reply");
        return {
            resCode: xhr.getResponseHeader('RCode'),
            errmessage: errMsg ? errMsg.text : "",
            message: reply ? reply.text : ""
        };
    }

    function setSigns(SCHEMENAME, setStatus, xhr) {
        var result;
        "use strict";
        // Если нет документов для подписи, прервать операцию.
        if (parseFloat('0' + getSignsReq.getResponseHeader("Sign-Count")) < 1) {
            var responseXML = $.parseXML(xhr.responseText);
            return {
                resCode: xhr.getResponseHeader('RCode'),
                errmessage: responseXML.find('errmsg').text(),
                message: responseXML.find('reply').text()
            };
        }
        var oXML = $.parseXML(getSignsReq.responseText);
        var n;
        var oSIc = oXML.selectNodes("//DOCS/R/SIGN/@SI"),
            oIDRc = oXML.selectNodes("//DOCS/R/@IDR"),
            j = oSIc.length, arSI = {}, oDR, sIDR;
        for (var i = 0; i < j; i++) {
            if (oSIc.item(i).text == '0') {
                sIDR = oIDRc.item(i).text;
                if (sIDR != '') arSI[oIDRc.item(i).text] = '0';
            }
        }
        var bSgCh;
        var mySigner = top.nv.MySigner;
        if (mySigner.XMLCryptoParamsData == '') {
            if (top.ReloadSignConf(top.mw)) {
                mySigner.XMLCryptoParamsData = top.SignConf.xml;
                mySigner.NumberOfSignatures = parseInt(top.SignConf.documentElement.attributes.getNamedItem('NOS').nodeValue);
            }
        }

        if (mySigner.NumberOfSignatures > 0) {
            mySigner.XMLCryptoParamsData = top.MyCrypto.XMLCryptoParamsData; // TT 91544
            if (mySigner.NumberOfSignatures == 1){
                mySigner.AutoSignMask = "1";
            }
            mySigner.DealSignaturesXML(oXML);
            bSgCh = mySigner.SignChanged;
        } else {
            var oN = oXML.selectNodes('/DT/DOCS/R');
            if (oN.length > 0)
                for (var i = 0; i < oN.length; i++) {
                    oN.item(i).selectSingleNode('SIGN/D').text = '';
                    oN.item(i).selectSingleNode('SIGN/@SI').nodeValue = 1331;
                }
        }

        if (n = oXML.selectSingleNode('/DT/SC/Signs'))n.parentNode.removeChild(n);
        for (var key in arSI) {
            oDR = oXML.selectSingleNode('//DOCS/R[@IDR="' + key + '"]');
            if (oDR != null && arSI[key] == '0' && oDR.selectSingleNode("SIGN/@SI").text == '0') oDR.parentNode.removeChild(oDR);
        }
        j = oXML.selectNodes("//DOCS/R").length;
        if (j < 1 || !bSgCh) {
            return {
                resCode: 5,
                message: ""
            };
        }

        var oFIELDS = oXML.selectNodes('//FIELDS');
        for (var i = 0; i < oFIELDS.length; i++) {
            oFIELDS.item(i).text = '';
        }
        var oReply = oXML.selectSingleNode('//reply');
        if (oReply != null)oReply.parentNode.removeChild(oReply);

        $(oXML).find("DOCS R SIGN").attr('SI', setStatus);

        $.ajax({
            type: "POST",
            url: top.scriptPath,
            data: oXML.xml,
            contentType: "text/xml; charset=UTF-8",
            headers: {'Rts-Request': 'SID=' + top.SID + '&T=RT_2IC.sc_set_signs&SCHEMENAME=' + SCHEMENAME + '&FILTERIDENT=NEW&XMLDATA=1'},
            async: false
        })
            .always(function (data, status, xhr) {
                result = getResult(data, xhr);
            });
        return result;
    }
}