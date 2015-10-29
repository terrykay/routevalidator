function formatToleranceValue(val) {
    if(val === 1000) return "1km";
    return val + "m";
}
$("#tolerance").slider({
    formatter: formatToleranceValue
});
$("#tolerance").on("slide", function(e) {
    $("#toleranceLabel").text(formatToleranceValue(e.value));
});

$(document).ready(function() {
    if($("#map").length > 0) {

    }
});