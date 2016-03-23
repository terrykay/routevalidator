<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    com.bjt.routevalidator.Result result = (com.bjt.routevalidator.Result)request.getAttribute("result");
    if(result == null)  {
        result = new com.bjt.routevalidator.Result(null, null, 200);
    }
    boolean isProcessed = result.isProcessed();
    String actualColour = "#f0c"; /* luminous pink (darkish) */
    String intendedColour = "#004cff"; /* blue */
%>

<!DOCTYPE html>
<html>
<head>
    <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/bootstrap-theme.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/bootstrap-slider.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="leaflet/leaflet.css" type="text/css"/>
    <link rel="stylesheet" href="css/site.css" type="text/css"/>
    <title>AUK Route Validator</title>
</head>

<body style="overflow-y:hidden">
<table class="mapcontainer">

    <tr class="mapheader">
        <td>
            <div>
            <form action="<%= result.getSubmitAction() %>" method="post"
                <% if (!isProcessed) { %> enctype="multipart/form-data" <% } %>
            >
            <table class="spaced">
                <tr>
                    <td><span>Intended GPX:</span></td>

                    <%  if (!isProcessed) { %>
                        <td><input class="filestyle" type="file" name="intended"/></td>
                    <% } else { %>
                        <td style="color: <%= intendedColour %>">${result.intendedGpx.fileName}</td>
                        <td><a href="." class="btn btn-primary">New enquiry</a></td>
                    <% } %>
                </tr>
                <tr>
                    <td><span>Actual GPX/TCX:</span></td>
                    <% if (!isProcessed) { %>
                        <td><input class="filestyle" type="file" name="actual" accept=".gpx,.tcx"/></td>
                    <% } else { %>
                        <td style="color: <%= actualColour %>">${result.actualGpx.fileName}</td>
                    <% } %>
                </tr>
                <tr>
                    <td><span>Tolerance (metres):</span></td>
                    <td>
                        <span id="toleranceLabel"><%= result.getToleranceString() %></span>
                        <input id="tolerance" name="tolerance" type="text"
                        data-slider-min="10" data-slider-max="1000" data-slider-step="10" data-slider-value="<%= result.getTolerance() %>"/>
                    </td>
                    <% if(isProcessed) { %>
                    <td>
                        <input class="btn btn-primary" type="submit" value="Recalculate" id="recalculate" style="visibility: hidden"/>
                    </td>
                    <% } %>
                </tr>
                <% if (isProcessed) { %>
                <tr>
                    <td><span>Result:</span></td>
                    <td><span class="result ${result.status}">${result.status}</span>
                    <% if (!result.getReferralAreas().isEmpty()) { %>
                    <td><span>Referral areas:</span>
                    <td>
                        <ul class="pagination" id="referralpages">
                            <li>
                              <a href="#" id="referralfirst" aria-label="First">
                                <span aria-hidden="true">&laquo;</span>
                              </a>
                            </li>
                            <li>
                              <a href="#" id="referralprev" aria-label="Previous">
                                <span aria-hidden="true">&lsaquo;</span>
                              </a>
                            </li>
                            <li><a href="#" id="referralcurrentpage" data-current="0"></a></li>
                            <li>
                              <a href="#" id="referralnext" aria-label="Next">
                                <span aria-hidden="true">&rsaquo;</span>
                              </a>
                            </li>
                            <li>
                              <a href="#" id="referrallast" aria-label="Last">
                                <span aria-hidden="true">&raquo;</span>
                              </a>
                            </li>
                          </ul>
                    </td>
                    <% } else {  %> <td></td><td></td> <% } %>
                </tr>
                <% } %>
                <% if(!isProcessed) { %>
                <tr>
                    <td><input class="btn btn-primary" type="submit" value="Compare"/></td>
                </tr>
                <% } %>
            </table>
            </form>
            <div class="logo"></div>
            </div>
        </td>
    </tr>
    <tr>
        <td class="mapcontainer">
            <div id="map">
            </div>
        </td>
    </tr>
</table>

<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-slider.min.js"></script>
<script type="text/javascript" src="js/bootstrap-filestyle.min.js"></script>
<script type="text/javascript" src="leaflet/leaflet-src.js"></script>
<script type="text/javascript" src="js/proj4-compressed.js"></script>
<script type="text/javascript" src="js/proj4leaflet.js"></script>
<script type="text/javascript" src="js/OSOpenSpace.js"></script>
<script type="text/javascript" src="js/site.js"></script>

<% if(result.getIntendedGpx() != null && result.getActualGpx() != null) { %>
<script type="text/javascript">
L.Map.prototype.setCrs = function(newCrs) {
    this.options.crs = newCrs;
}
$(document).ready(function() {
        var map = L.map("map", {
            continuousWorld: true,
            worldCopyJump: false,
            minZoom: 0
        });
        var defaultCrs = map.options.crs;

        var mapboxLayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
            maxZoom: 18,
            id: 'bentaylor.o16m82k1',
            accessToken: 'pk.eyJ1IjoiYmVudGF5bG9yIiwiYSI6Ik5WRF95TXcifQ.h24LeDgvQobB_uwKymYbTA'
        });

        var openspaceLayer = L.tileLayer.OSOpenSpace("229B0D5190F91C32E0530B6CA40A00BA");
	openspaceLayer.setOpacity(0.3);

        map.on('baselayerchange', function(layer) {
              var centerPoint = map.getCenter();
              var startZoom = map.getZoom() - (map.currentZoomDelta || 0);
              var zoomDelta = 0;
              if(layer.name == osLayerName) {
                zoomDelta = -6;
                map.setCrs(L.OSOpenSpace.getCRS());
              } else {
                zoomDelta = 0;
                map.setCrs(defaultCrs);
              }
              map.currentZoomDelta = zoomDelta;
              var newZoom = startZoom + zoomDelta;
              if(newZoom < 0) newZoom = 0;
              if(map.getMaxZoom()  && newZoom > map.getMaxZoom()) newZoom = map.getMaxZoom();
              map.setView(centerPoint, newZoom);
        });
        map.addLayer(mapboxLayer);

        var osLayerName = "Ordnance Survey";
        var baseMaps = {
            "MapBox": mapboxLayer
        };
        baseMaps[osLayerName] = openspaceLayer;
        L.control.layers(baseMaps).addTo(map);

        var intended = L.multiPolyline(<%= result.getIntendedGpx().getSimpleLatLngArray() %>
            , {color: '<%= intendedColour %>', weight: 10 } ).addTo(map);

        var actual = L.multiPolyline(<%= result.getActualGpx().getSimpleLatLngArray() %>
            , {color: '<%= actualColour %>', weight: 10 } ).addTo(map);


        var fitBoundsOptions = { maxZoom: 13};
        var referralAreas = <%= result.getReferralAreasString() %>;
        if(referralAreas && referralAreas.length > 0) {
            $("#referralcurrentpage").text("1 of " + referralAreas.length);
            map.fitBounds(referralAreas[0], fitBoundsOptions );
            $("#referralfirst").click(function() { setReferral(0);});
            $("#referralprev").click(function() {setReferral(parseInt($("#referralcurrentpage").data("current")) - 1);});
            $("#referralcurrentpage").click(function() {setReferral(parseInt($("#referralcurrentpage").data("current")));});
            $("#referralnext").click(function() {setReferral(parseInt($("#referralcurrentpage").data("current")) + 1);});
            $("#referrallast").click(function() {setReferral(referralAreas.length - 1); });
        }
        var featureGroup = new L.featureGroup([intended, actual]);
        map.fitBounds(featureGroup);

        var setReferral = function(j) { //j is 0-based
            $("#referralpages a").trigger("blur");
            var i = (j + referralAreas.length) % (referralAreas.length);
            if(i >= 0 && i < referralAreas.length) {
                $("#referralcurrentpage").data("current", i).text((i + 1) + " of " + referralAreas.length);
                map.fitBounds(referralAreas[i], fitBoundsOptions);
            }
        };

        $("#tolerance").on("slide", function() {
            $("#recalculate").show().css("visibility", "visible");
        });
});
</script>
<% } %>
</body>
</html>
