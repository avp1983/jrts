var frameActivityMonitor = function (frameElem, frameElemJq, actionCallback) {
    actionCallback();
    var frameElemDoc = frameElem.document;
    var eventsCollection = 'keydown.ActivityMonitor click.ActivityMonitor';
    var bindFunc = function () {
        try{
            frameElemJq(frameElemDoc).bind(eventsCollection, function () {
                actionCallback();
                frameElemJq(frameElemDoc).unbind(eventsCollection);
                setTimeout(function () {
                    bindFunc();
                }, 10 * 1000);
            });

        }
        catch (e){
        }
    };
    bindFunc();

    frameElemJq(frameElem).unload(function () {
        setTimeout(function () {
            try {
                new frameActivityMonitor(top.mw, top.mw.$ ? top.mw.$ : top.$, actionCallback);
            }
            catch (e) {
            }
        }, 10 * 1000);
    });

    frameElemJq(frameElem).find("iframe").each(function () {
        setTimeout(function(){
            new frameActivityMonitor(this.contentWindow, frameElemJq, actionCallback);
        }, 5000);
    });
};
function GlobalIdleMonitor(toggleTimeout, siteIdleCallback) {
    var milisecFromLastActivity = 0;
    var checkActivityTimeout = 5000;
    this.start = function () {
        var siteActivityCheckInterval = setInterval(function () {
            milisecFromLastActivity += checkActivityTimeout;
            if (milisecFromLastActivity > toggleTimeout) {
                siteIdleCallback();
                clearInterval(siteActivityCheckInterval);
            }
        }, checkActivityTimeout);
    };
    this.reset = function(){
        milisecFromLastActivity = 0;
    };

    this.wasActivity = function () {
        milisecFromLastActivity = 0;
    };

    var self = this;
    var waitAllSiteInterval = setInterval(function(){
        if (!top.tb.$ || !top.taskbar.$ || !top.$){
            return;
        }
        clearInterval(waitAllSiteInterval);
        new frameActivityMonitor(top.tb, top.tb.$, self.wasActivity);
        new frameActivityMonitor(top.mw, top.mw.$ ? top.mw.$ : top.$, self.wasActivity);
        new frameActivityMonitor(top.taskbar, top.taskbar.$, self.wasActivity);
        new frameActivityMonitor(top, top.$, self.wasActivity);
    }, 2000);
}