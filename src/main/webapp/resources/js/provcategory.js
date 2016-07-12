$(function () {
    var $widgets = $(".ui-dashboard .ui-panel");
    for (i = 0; i < $widgets.length; i++) {
        var $table = $widgets.eq(i).find(".widgetWPC");
        if (!$table.get(0)) {
            $widgets.eq(i).fadeOut("slow");
        }
    }
});
