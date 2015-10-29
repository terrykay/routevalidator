<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bjt.RouteValidator.Result" %>
<!DOCTYPE html>
<html>
<head>
    <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/bootstrap-theme.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/bootstrap-slider.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="leaflet/leaflet.css" type="text/css"/>
    <link rel="stylesheet" href="css/site.css" type="text/css"/>
</head>
<body>
<table class="mapcontainer">
    <tr class="mapheader">
        <td>
            <table class="spaced">
                <tr>
                    <td><span>Intended GPX:</span></td>
                    <td>${result.intendedGpx.fileName}</td>
                    <td></td>
                </tr>
                <tr>
                    <td><span>Actual GPX:</span></td>
                    <td>${result.actualGpx.fileName}</td>
                    <td></td>
                </tr>
                <tr>
                    <td><span>Tolerance (metres):</span></td>
                    <td>
                        <span>${result.tolerance}m</span>
                        <!--
                        <span id="toleranceLabel">200m</span>
                        <input id="tolerance" name="tolerance" type="text" data-slider-min="10" data-slider-max="1000" data-slider-step="10" data-slider-value="200"/>
                        -->
                    </td>
                </tr>
                <tr>
                    <td><span>Result:</span></td>
                    <td><span class="result ${result.status}">${result.status}</span>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <div id="map">
            </div>
        </td>
    </tr>
</table>
<script type="text/javascript">
$(document).ready(function() {
        var map = L.map("map");
        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'bentaylor.o16m82k1',
            accessToken: 'pk.eyJ1IjoiYmVudGF5bG9yIiwiYSI6Ik5WRF95TXcifQ.h24LeDgvQobB_uwKymYbTA'
        }).addTo(map);
        <% Result result = request.getAttribute("result"); %>
        var intended = L.multiPolyline(<%= result.getTolerance() %>
            , {color: 'blue' } ).addTo(map);


        var featureGroup = new L.featureGroup([intended]);
        map.fitBounds(featureGroup);

});
</script>
<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-slider.min.js"></script>
<script type="text/javascript" src="leaflet/leaflet.js"></script>
<script type="text/javascript" src="js/proj4-compressed.js"></script>
<script type="text/javascript" src="js/proj4leaflet.js"></script>
<script type="text/javascript" src="js/OSOpenSpace.js"></script>
<script type="text/javascript" src="js/site.js"></script>
</body>
</html>