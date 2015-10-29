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

<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrap-slider.min.js"></script>
<script type="text/javascript" src="leaflet/leaflet.js"></script>
<script type="text/javascript" src="js/proj4-compressed.js"></script>
<script type="text/javascript" src="js/proj4leaflet.js"></script>
<script type="text/javascript" src="js/OSOpenSpace.js"></script>
</body>
</html>