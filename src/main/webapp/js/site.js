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
        var map = L.map("map").setView([51.505, -0.09], 13);
        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'bentaylor.o16m82k1',
            accessToken: 'pk.eyJ1IjoiYmVudGF5bG9yIiwiYSI6Ik5WRF95TXcifQ.h24LeDgvQobB_uwKymYbTA'
        }).addTo(map);
    }
});