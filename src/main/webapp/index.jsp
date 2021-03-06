<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    com.bjt.routevalidator.Result result = (com.bjt.routevalidator.Result)request.getAttribute("result");
    if(result == null)  {
        result = new com.bjt.routevalidator.Result();
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
    <link href="css/bootstrap-slider.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/site.css" type="text/css" rel="stylesheet"/>
    <link href="css/jquery.jgrowl.min.css" type="text/css" rel="stylesheet"/>
    <link href="css/tooltipster.css" type="text/css" rel="stylesheet"/>
    <title>AUK Route Validator</title>
</head>

<body>
<table class="mapcontainer">

    <tr class="mapheader">
        <td>
            <div>
            <form action="<%= result.getSubmitAction() %>" method="post"
                <% if (!isProcessed) { %> enctype="multipart/form-data" <% } %>
            >

                <div style="float: left">
                    <table class="spaced">
                        <tr>
                            <td><span>Intended GPX:</span></td>

                            <%  if (!isProcessed) { %>
                                <td><input class="filestyle" type="file" name="intended" accept=".gpx,.zip"/></td>
                            <% } else { %>
                                <td style="color: <%= intendedColour %>"><%= result.getIntendedGpx().getShortFileName() %></td>
                            <% } %>
                        </tr>
                        <tr>
                            <td><span>Actual GPX/TCX:</span></td>
                            <% if (!isProcessed) { %>
                                <td><input class="filestyle" type="file" name="actual" accept=".gpx,.tcx,.zip"/></td>
                            <% } else { %>
                                <td style="color: <%= actualColour %>"><%= result.getActualGpx().getShortFileName() %></td>
                            <% } %>
                        </tr>
                        <% if (result.hasTrackUsePreferences()) { %>
                        <tr>
                            <td style="vertical-align: top"><span>Track list:</span></td>
                            <td>
                                <table class="internalpadding5">
                                    <tbody>
                                    <%
                                     Integer count = 0;
                                     for(com.bjt.routevalidator.TrackUsePreference trackUsePreference : result.getTrackUsePreferences()) {
                                      %>
                                    <tr>
                                        <td><span><%= trackUsePreference.getTrackName() %></span></td>
                                        <td>
                                            <% if (trackUsePreference.isRender()) { %>
                                                <input type="checkbox" name="<%= String.format("trackusepreference_checked_%d", count) %>" checked="checked"/>
                                            <% } else { %>
                                                <input type="checkbox" name="<%= String.format("trackusepreference_checked_%d", count) %>"/>
                                            <% } %>
                                            <input type="hidden" name="<%= String.format("trackusepreference_name_%d", count) %>" value="<%=trackUsePreference.getTrackName() %>"/>
                                        </td>
                                    </tr>
                                    <% count++; } %>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <% } %>

                        <% if(!isProcessed) { %>
                        <tr>
                            <td><input class="btn btn-primary" type="submit" value="Compare"/></td>
                        </tr>
                        <% } %>

                    </table>
                </div>
                <div style="float: left">
                    <table class="spaced">
                        <tr>
                            <td><span>Tolerance (metres):</span></td>
                            <td>
                                <span id="toleranceLabel"><%= result.getToleranceString() %></span>
                                <input id="tolerance" name="tolerance" type="text"
                                data-slider-tooltip-position="bottom"
                                data-slider-min="10" data-slider-max="1000" data-slider-step="10" data-slider-value="<%= result.getTolerance() %>"/>
                            </td>
                        </tr>
                        <% if (isProcessed) { %>
                        <tr>
                            <td><span>Track adherence:</span></td>
                            <td><span class="result ${result.status}">${result.status}</span>
                        </tr>
                        <% for (com.bjt.routevalidator.TableCell[] acceptanceRow : result.getAcceptanceRows()) { %>
                            <tr>
                                <% for(com.bjt.routevalidator.TableCell cell : acceptanceRow) { %>
                                     <td colspan="<%= cell.getColSpan() %>" class="<%= cell.getClasses() %>">
                                         <span><%= cell.getContents() %></span>
                                     </td>
                                <% } %>
                            </tr>
                        <% } %>

                        <% if (!result.getReferralAreas().isEmpty()) { %>
                        <tr>
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

                        </tr>
                        <% } %> <%-- if getReferralAreas isEmpty --%>
                        <%-- speed limit compliance tr  --%>
                        <% } %> <%-- if isProcessed --%>
                    </table>
                </div>

                <% if (isProcessed) { %>
                <div style="float: left; margin-left: 10px"> <%-- buttons --%>
                    <div style="margin-top: 10px">
                        <a href="." class="btn btn-primary">New enquiry</a>
                    </div>
                    <div style="margin-top: 10px">
                        <input class="btn btn-primary" type="submit" value="Recalculate" id="recalculate" style="visibility: hidden"/>
                    </div>
                </div>
                <% } %>

                <div style="clear: both"></div>
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
        <% if(isProcessed) { %>
        <td style="vertical-align: top">
            <div id="stats">
                <table id="intendedstats">
                    <thead>
                        <tr>
                            <th colspan="2"><strong>Intended track</strong></th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for(com.bjt.routevalidator.Statistic stat : result.getIntendedStatistics()) { %>
                            <tr>
                                <% for (com.bjt.routevalidator.TableCell cell : stat.getCells()) { %>
                                    <td colspan="<%= cell.getColSpan() %>">
                                        <span><%= cell.getContents() %></span>
                                    </td>
                                <% } %>
                            </tr>
                        <% } %>
                        <tr>
                            <td colspan="2"><button class="btn btn-primary" id="showintendedonly">Show Stats for Intended GPX only</button></td>
                        </tr>
                        <tr>
                            <td colspan="2"><button class="btn btn-primary" id="copybutton"
                                data-copy="Copy stats to clipboard"
                                data-copied="Copied!"
                                data-clipboard-target="#actualstats">Copy Stats to clipboard</button></td>
                        </tr>
                        <% final String mailToHref = result.getMailtoHref(request); if(mailToHref != null && !mailToHref.isEmpty()) { %>
                        <tr>
                            <td colspan="2"><a class="btn btn-primary" href="<%= mailToHref %>">Refer to AAA</button></td>
                        </tr>
                        <% } %>
                        <tr>
                            <td colspan="2"><a href="downloadworkings" target="blank" title="Might not contain the same number of rows as trackpoints due to the way ValidateGPX's algorithm works.">Download workings</a></td>
                        </tr>
                    </tbody>
                </table>
                <table style="float:left; margin-left: 20px; max-width: 330px" id="actualstats">
                    <thead>
                        <tr class="clipboarditems onlyvisiblewhennotcopying">
                            <th colspan="2"><strong>Actual track</strong></th>
                        </tr>
                        <tr class="clipboarditems onlyvisiblewhencopying">
                            <th colspan="2"><%= result.getIntendedGpx().getFileName() %></th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for(com.bjt.routevalidator.TableCell[] cells : result.getPreliminaryRows()) { %>
                            <tr class="clipboarditems onlyvisiblewhencopying">
                                <% for (com.bjt.routevalidator.TableCell cell : cells) { %>
                                    <td colspan="<%= cell.getColSpan() %>">
                                        <span><%= cell.getContents() %></span>
                                    </td>
                                <% } %>
                            </tr>
                        <% } %>
                        <% for(com.bjt.routevalidator.Statistic stat : result.getActualStatistics()) { %>
                            <tr>
                                <% for (com.bjt.routevalidator.TableCell cell : stat.getCells()) { %>
                                    <td colspan="<%= cell.getColSpan() %>">
                                        <span><%= cell.getContents() %></span>
                                    </td>
                                <% } %>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
                <div style="clear: both"></div>
            </div>
            <div id="elevationgraph" style="width: 100%; height: 220px">

            </div>
        </td>
        <% } %>
    </tr>
</table>

<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-slider.min.js"></script>
<script type="text/javascript" src="js/bootstrap-filestyle.min.js"></script>
<script type="text/javascript" src="leaflet/leaflet-src.js"></script>
<script type="text/javascript" src="js/leaflet.geometryutil.js"></script>
<script type="text/javascript" src="js/proj4-compressed.js"></script>
<script type="text/javascript" src="js/proj4leaflet.js"></script>
<script type="text/javascript" src="js/OSOpenSpace.js"></script>
<script type="text/javascript" src="js/site.js"></script>
<script type="text/javascript" src="js/jquery.jgrowl.min.js"></script>
<script type="text/javascript" src="js/clipboard.js"></script>
<script type="text/javascript" src="js/jquery.tooltipster.min.js"></script>
<script type="text/javascript" src="js/underscore-min.js"></script>


<%
    String errorMessage = (String)request.getSession().getAttribute("FriendlyErrorMessage");
    if(!(errorMessage == null || errorMessage.isEmpty())) {
%>
        <script type="text/javascript">
            $(document).ready(function() {
                $.jGrowl('<%= org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(errorMessage) %>', {sticky: true, theme: "error", position: "center"});
            });
        </script>
<%
    } /* if error message */
%>

<%
   if(!result.getWarnings().isEmpty()) {
%>
        <script type="text/javascript">
            $(document).ready(function() {
            <% for(final String warning : result.getWarnings()) { %>
                $.jGrowl('<%= org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(warning) %>', {sticky: true, theme: "error", position: "center"});
            <% } %>
            });
        </script>
<%
    } /* if warnings */
%>



<% if(result.getIntendedGpx() != null && result.getActualGpx() != null ) { %>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script type="text/javascript">

$(document).on("click", "#showintendedonly", function() {
    if($("#actualstats").css("visibility") == "visible") {
       	$("#actualstats").css("visibility", "hidden");
    } else {
       $("#actualstats").css("visibility", "visible");
    }
});

<% if(result.getActualGpx().getGpx().getTracks().size() > 0) { %>

      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);


      function drawChart() {
        var data = google.visualization.arrayToDataTable( <%= result.getAltitudeGraphJson() %> );
    //get ideal height for chart:
            var idealHeight = $(window).innerHeight() - $("#elevationgraph").position().top - 10;
            if(idealHeight < 100) idealHeight = 100;
            if(idealHeight > 250) idealHeight = 250;
            $("#elevationgraph").css("height", idealHeight + "px");

        var options = {
          legend: 'none',
          chartArea: {
          	left: 30,
            top: 10,
            width: '100%',
            height: (idealHeight - 30)
          }
        };

        var chart = new google.visualization.LineChart(document.getElementById('elevationgraph'));

        chart.draw(data, options);
      }
<% } %>

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
        var distancePoints = <%= result.getDistancePointsJson() %> ;
        var featureGroup = new L.featureGroup([intended, actual]);
        var distanceTooltip = L.popup();
        featureGroup.on("mouseover", function(e) {
            featureGroup.shouldShowTooltip = true;
        });
        featureGroup.on("mousemove", _.throttle(function(e) {
                if(featureGroup.shouldShowTooltip === true) {
                    var closest = L.GeometryUtil.closest(map, distancePoints, e.latlng, true);
                    var index = closest.index;
                    var distancePoint = distancePoints[index];
                    distanceTooltip.setLatLng(e.latlng).setContent(distancePoint.label);
                    if(distanceTooltip._isOpen !== true) distanceTooltip.openOn(map);
                }
            }, 500, {leading: false})
        );
        featureGroup.on("mouseout", function(e) {
            featureGroup.shouldShowTooltip = false;
        });

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

        $("input[type=checkbox]").on("change", function() {
            $("#recalculate").show().css("visibility", "visible");
        });

        var $el = $("#copybutton");
        var clipboard = new Clipboard("#copybutton");
        var tt = instance = $el.tooltipster({
                        content: $el.attr('data-copy'),
                        trigger: 'custom',
                        triggerClose: { mouseleave: true },
                        triggerOpen: { hover: true },
                        position: 'right'
                    });
        var instance = tt.tooltipster('instance');

            clipboard
                .on('success', function(e) {
                    instance
                        .content($el.attr('data-copied'))
                        .one('after', function(){
                            instance.content($el.attr('data-copy'));
                        });
                    window.getSelection().removeAllRanges();
                })
                .on('error', function(e) {
                    instance
                        .content($el.attr('data-copyerror'))
                        .one('after', function(){
                            instance.content($el.attr('data-copy'));
                        });
                });

        $("a[title]").tooltipster();
});
</script>
<% } %>

</body>
</html>
<!--Taken ${taken} m/s-->
