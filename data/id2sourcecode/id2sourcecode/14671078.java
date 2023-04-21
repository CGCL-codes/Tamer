    protected boolean writeOziMapFile(String directory, String imageFilename, String mapFilename) {
        boolean success = true;
        MapImageParameters imageParams = getMapImageParameters();
        if (imageParams == null) return false;
        double minx = imageParams.getMinX();
        double miny = imageParams.getMinY();
        double maxx = imageParams.getMaxX();
        double maxy = imageParams.getMaxY();
        int width = imageParams.getWidth();
        int height = imageParams.getHeight();
        double unitMultiplier = 1.0d;
        boolean useLatLongForGrid = false;
        if (cs.getProjection().hasNameForRealm(MRCoordinateSystem.REALM_OZI_EXPLORER_UNIT_MULTIPLIER)) {
            unitMultiplier = Double.parseDouble(cs.getProjection().getName(MRCoordinateSystem.REALM_OZI_EXPLORER_UNIT_MULTIPLIER));
        }
        if (cs.getProjection().hasNameForRealm(MRCoordinateSystem.REALM_OZI_EXPLORER_USE_LATLONG_FOR_GRID)) {
            useLatLongForGrid = new Boolean(cs.getProjection().getName(MRCoordinateSystem.REALM_OZI_EXPLORER_USE_LATLONG_FOR_GRID)).booleanValue() && cs.isKnownCoordinateSystem();
        }
        try {
            FileWriter out = new FileWriter(directory + File.separator + mapFilename, false);
            out.write("OziExplorer Map Data File Version 2.2\r\n");
            out.write(imageFilename + "\r\n");
            out.write(directory + File.separator + imageFilename + "\r\n");
            out.write("1 ,Map Code,\r\n");
            out.write(cs.getDatum().getName(cs.REALM_OZI_EXPLORER) + "," + cs.getDatum().getName(cs.REALM_OZI_EXPLORER_CODE) + ",   0.0000,   0.0000," + cs.getDatum().getName(cs.REALM_OZI_EXPLORER_CODE) + "\r\n");
            out.write("Reserved 1\r\n");
            out.write("Reserved 2\r\n");
            out.write("Magnetic Variation,,,E\r\n");
            out.write("Map Projection," + cs.getProjection().getName(cs.REALM_OZI_EXPLORER) + ",PolyCal,No,AutoCalOnly,No,BSBUseWPX,No\r\n");
            CoordinatePoint wgs84 = null;
            double x;
            double y;
            CoordinatePoint point;
            if (cs.getProjection().isLatLong() || useLatLongForGrid) {
                if (useLatLongForGrid) {
                    point = cs.convertToLatLong(new CoordinatePoint(minx, maxy));
                    x = point.getOrdinate(0);
                    y = point.getOrdinate(1);
                } else {
                    x = minx;
                    y = maxy;
                }
                out.write("Point01,xy,0,0,in, deg," + extractDegrees(y) + "," + extractMinutes(y) + "," + ((y > 0) ? "N" : "S") + "," + extractDegrees(x) + "," + extractMinutes(x) + "," + ((x > 0) ? "E" : "W") + "," + "grid, , , ,\r\n");
                if (useLatLongForGrid) {
                    point = cs.convertToLatLong(new CoordinatePoint(maxx, miny));
                    x = point.getOrdinate(0);
                    y = point.getOrdinate(1);
                } else {
                    x = maxx;
                    y = miny;
                }
                out.write("Point02,xy," + (width - 1) + "," + (height - 1) + ",in, deg," + extractDegrees(y) + "," + extractMinutes(y) + "," + ((y > 0) ? "N" : "S") + "," + extractDegrees(x) + "," + extractMinutes(x) + "," + ((x > 0) ? "E" : "W") + "," + "grid, , , ,\r\n");
            } else {
                String zone;
                String hemisphere;
                point = new CoordinatePoint(minx, maxy);
                zone = cs.getProjection().getZone(point);
                if (cs.getProjection().getName().equalsIgnoreCase("bng")) {
                    x = BritishGrid.stripMajorDigit(minx);
                    y = BritishGrid.stripMajorDigit(maxy);
                } else {
                    x = minx;
                    y = maxy;
                }
                if (cs.isKnownCoordinateSystem()) {
                    wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(minx, maxy));
                    hemisphere = (wgs84.getOrdinate(1) >= 0.0) ? "N" : "S";
                } else {
                    hemisphere = cs.getProjection().getHemisphere();
                }
                out.write("Point01,xy,0,0,in, deg, , , , , , , grid," + zone + "," + (x * unitMultiplier) + "," + (y * unitMultiplier) + "," + hemisphere + "\r\n");
                point = new CoordinatePoint(maxx, miny);
                zone = cs.getProjection().getZone(point);
                if (cs.getProjection().getName().equalsIgnoreCase("bng")) {
                    x = BritishGrid.stripMajorDigit(maxx);
                    y = BritishGrid.stripMajorDigit(miny);
                } else {
                    x = maxx;
                    y = miny;
                }
                if (cs.isKnownCoordinateSystem()) {
                    wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(maxx, miny));
                    hemisphere = (wgs84.getOrdinate(1) >= 0.0) ? "N" : "S";
                } else {
                    hemisphere = cs.getProjection().getHemisphere();
                }
                out.write("Point02,xy," + (width - 1) + "," + (height - 1) + ",in, deg, , , , , , , grid," + zone + "," + (x * unitMultiplier) + "," + (y * unitMultiplier) + "," + hemisphere + "\r\n");
            }
            for (int loop = 3; loop <= 30; loop++) {
                out.write("Point" + ((loop < 10) ? "0" : "") + loop + ",xy,     ,     ,ex, deg,    ,        ,,    ,        ,, grid,   ,           ,           ,\r\n");
            }
            if (cs.getProjection().hasNameForRealm(MRCoordinateSystem.REALM_OZI_EXPLORER_PROJECTION_PARAMS)) {
                String projectionSetup = cs.getProjection().getName(MRCoordinateSystem.REALM_OZI_EXPLORER_PROJECTION_PARAMS);
                out.write("Projection Setup," + projectionSetup + "\r\n");
            } else {
                out.write("Projection Setup,,,,,,,,,,\r\n");
            }
            out.write("Map Feature = MF ; Map Comment = MC     These follow if they exist\r\n");
            out.write("Track File = TF      These follow if they exist\r\n");
            out.write("Moving Map Parameters = MM?    These follow if they exist\r\n");
            out.write("MM0,Yes\r\n");
            out.write("MMPNUM,4\r\n");
            out.write("MMPXY,1,0,0\r\n");
            out.write("MMPXY,2," + (width - 1) + ",0,\r\n");
            out.write("MMPXY,3," + (width - 1) + "," + (height - 1) + "\r\n");
            out.write("MMPXY,4,0," + (height - 1) + "\r\n");
            if (cs.isKnownCoordinateSystem()) {
                wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(minx, maxy));
                out.write("MMPLL,1," + wgs84.getOrdinate(0) + "," + wgs84.getOrdinate(1) + "\r\n");
                wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(maxx, maxy));
                out.write("MMPLL,2," + wgs84.getOrdinate(0) + "," + wgs84.getOrdinate(1) + "\r\n");
                wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(maxx, miny));
                out.write("MMPLL,3," + wgs84.getOrdinate(0) + "," + wgs84.getOrdinate(1) + "\r\n");
                wgs84 = cs.convertToLatLongWGS84(new CoordinatePoint(minx, miny));
                out.write("MMPLL,4," + wgs84.getOrdinate(0) + "," + wgs84.getOrdinate(1) + "\r\n");
                double midy = (miny + maxy) / 2;
                double xsize = (maxx - minx) / width;
                double midx = (minx + maxx) / 2;
                CoordinatePoint point1 = cs.convertToLatLongWGS84(new CoordinatePoint(midx, midy));
                CoordinatePoint point2 = cs.convertToLatLongWGS84(new CoordinatePoint(midx + xsize, midy));
                Ellipsoid wgs84ellipsoid = GeocentricCoordinateSystem.DEFAULT.getHorizontalDatum().getEllipsoid();
                double distance = wgs84ellipsoid.orthodromicDistance(point1.getOrdinate(0), point1.getOrdinate(1), point2.getOrdinate(0), point2.getOrdinate(1));
                out.write("MM1B," + distance + "\r\n");
            }
            out.write("MOP,Map Open Position,0,0\r\n");
            out.write("IWH,Map Image Width/Height," + width + "," + height + "\r\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
