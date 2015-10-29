<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="leaflet/leaflet.css" type="text/css"/>
    <link rel="stylesheet" href="css/site.css" type="text/css"/>
</head>
<body>
<table class="mapcontainer">
    <tr class="mapheader">
        <td>
            <div>
                    With a tolerance of ${result.tolerance} the answer is: ${result.status}
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="map">
            </div>
        </td>
    </tr>
</table>

<!--
<div class="mapcontainer">
    <div class="mapheader">
        With a tolerance of ${result.tolerance} the answer is: ${result.status}
    </div>

    <div id="map">
    </div>
</div>
-->
<script src="leaflet/leaflet.js" type="text/javascript"></script>
<script src="js/proj4-compressed.js" type="text/javascript"></script>
<script src="js/proj4leaflet.js" type="text/javascript"></script>
<script src="js/OSOpenSpace.js" type="text/javascript"></script>
</body>
</html>