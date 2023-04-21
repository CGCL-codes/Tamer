    public void coordinatesChanged() {
        double minlat;
        double maxlat;
        double minlong;
        double maxlong;
        try {
            minlat = Double.parseDouble(minLat.text());
            maxlat = Double.parseDouble(maxLat.text());
            minlong = Double.parseDouble(minLong.text());
            maxlong = Double.parseDouble(maxLong.text());
            double lat = (minlat + maxlat) / 2;
            double lon = (minlong + maxlong) / 2;
            double latMin = Math.log(Math.tan(Math.PI / 4.0 + minlat / 180.0 * Math.PI / 2.0)) * 180.0 / Math.PI;
            double latMax = Math.log(Math.tan(Math.PI / 4.0 + maxlat / 180.0 * Math.PI / 2.0)) * 180.0 / Math.PI;
            double size = Math.max(Math.abs(latMax - latMin), Math.abs(maxlong - minlong));
            int zoom = 0;
            while (zoom <= 20) {
                if (size >= 180) {
                    break;
                }
                size *= 2;
                zoom++;
            }
            osmURL.setText("http://www.openstreetmap.org/index.html?mlat=" + lat + "&mlon=" + lon + "&zoom=" + zoom);
        } catch (Exception e) {
        }
    }
