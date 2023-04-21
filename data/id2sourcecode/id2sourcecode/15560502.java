    private Grid makeZoneGrid() {
        GridCell gridCell = null;
        ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
        ZONE_CELLS = new ArrayList<GridCell>();
        double sLat = m_bbox.getMinLat();
        double wLng = m_bbox.getMinLon();
        double nLat = m_bbox.getMaxLat();
        double eLng = m_bbox.getMaxLon();
        if (wLng < 12 && eLng > 335) {
            wLng = -180;
            eLng = 180;
        }
        double x1 = (Math.floor((wLng / 6) + 1) * 6.0);
        double y1;
        if (sLat < -80) {
            y1 = -80;
        } else {
            y1 = (Math.floor((sLat / 8) + 1) * 8.0);
        }
        ArrayList<Double> lat_coords = new ArrayList<Double>();
        ArrayList<Double> lng_coords = new ArrayList<Double>();
        if (sLat < -80) {
            lat_coords.add(0, -80.0);
        } else {
            sLat = Math.floor(sLat / 8) * 8;
            lat_coords.add(0, sLat);
        }
        int j;
        double lat, lng;
        if (nLat > 80) {
            nLat = 80;
        }
        for (lat = y1, j = 1; lat < nLat; lat += 8, j++) {
            if (lat <= 72) {
                lat_coords.add(j, lat);
            } else if (lat <= 80) {
                lat_coords.add(j, 84.0);
            } else {
                j--;
            }
        }
        nLat = Math.ceil(nLat / 8) * 8;
        lat_coords.add(j, nLat);
        wLng = Math.floor(wLng / 6) * 6;
        lng_coords.add(0, wLng);
        if (wLng < eLng) {
            for (lng = x1, j = 1; lng < eLng; lng += 6, j++) {
                lng_coords.add(j, lng);
            }
        } else {
            for (lng = x1, j = 1; lng <= 180; lng += 6, j++) {
                lng_coords.add(j, lng);
            }
            for (lng = -180; lng < eLng; lng += 6, j++) {
                lng_coords.add(j, lng);
            }
        }
        eLng = Math.ceil(eLng / 6) * 6;
        lng_coords.add(j++, eLng);
        for (int i = 0; i < lat_coords.size() - 1; i++) {
            for (j = 0; j < lng_coords.size() - 1; j++) {
                double latA = lat_coords.get(i);
                double latB = lat_coords.get(i + 1);
                double lngA = lng_coords.get(j);
                double lngB = lng_coords.get(j + 1);
                Point sw = new Point(latA, lngA);
                Point se = new Point(latA, lngB);
                Point ne = new Point(latB, lngB);
                Point nw = new Point(latB, lngA);
                gridCell = new GridCell(sw, se, ne, nw);
                if (latA != latB) {
                    if (latA == 56 && lngA == 6) {
                        lngA = 3;
                    } else if (latA == 56 && lngA == 0) {
                        lngB = 3;
                    }
                    double centerLat = (latA + latB) / 2;
                    double centerLng = (lngA + lngB) / 2;
                    Point center = new Point(centerLat, centerLng);
                    String name = getGridLabelFromPoint(center);
                    if (name != null) {
                        gridCell.setLabel(center, name.substring(0, 3));
                    }
                }
                gridCells.add(gridCell);
                if (gridCell.isFullSized()) {
                    ZONE_CELLS.add(gridCell);
                }
            }
        }
        Grid grid = new Grid(gridCells);
        ArrayList<Line> lines = handleSpecialCases(lat_coords, lng_coords);
        grid.getLngLines().addAll(lines);
        return grid;
    }
