    private GeoShape readPolyline(boolean isClosed, char ptType) throws IOException {
        readDouble(shpStream);
        readDouble(shpStream);
        readDouble(shpStream);
        readDouble(shpStream);
        int nParts = readInt(shpStream);
        int nPoints = readInt(shpStream);
        int parts[] = new int[nParts];
        for (int i = 0; i < nParts; i++) {
            parts[i] = readInt(shpStream);
        }
        int nums[] = new int[nParts];
        for (int i = 0; i < nParts; i++) {
            if (i != nParts - 1) {
                nums[i] = parts[i + 1] - parts[i];
            } else {
                nums[i] = nPoints - parts[i];
            }
        }
        GeoCoord[] coords = new GeoCoord[nPoints];
        for (int i = 0; i < nPoints; i++) {
            double lon = readDouble(shpStream);
            double lat = readDouble(shpStream);
            coords[i] = new GeoCoord(lat, lon);
        }
        if (ptType == 'Z') {
            double[] alts = readDoubleRange(nPoints);
            for (int i = 0; i < nPoints; i++) {
                coords[i].setAltitude(alts[i]);
            }
        }
        if (ptType == 'M' || ptType == 'Z') {
            readDoubleRange(nPoints);
        }
        GeoMultiShape multi = new GeoMultiShape();
        for (int i = 0; i < nParts; i++) {
            int partPts = nums[i];
            GeoCoord[] polyCoords = new GeoCoord[partPts];
            for (int j = 0; j < partPts; j++) {
                polyCoords[j] = coords[parts[i] + j];
            }
            if (isClosed) {
                multi.add(new GeoPolygon(polyCoords));
            } else {
                multi.add(new GeoPolyline(polyCoords));
            }
        }
        return (multi.size() == 1) ? multi.get(0) : multi;
    }
