    public static Map getMaps(double lat, double lon, int zoom) throws MalformedURLException, IOException {
        System.out.println("Get map : " + lat + "    " + lon);
        int latitudeTileNumber = lat2tile(lat, zoom);
        int longitudeTileNumber = lon2tile(lon, zoom);
        int[] tileCoord = { 0, 0 };
        int[] coord = { 0, 0 };
        Bitmap[][] maps = new Bitmap[3][3];
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                try {
                    tileCoord[0] = longitudeTileNumber + j - 1;
                    tileCoord[1] = latitudeTileNumber + i - 1;
                    URL url = new URL(("http://tile.openstreetmap.org/" + zoom + "/" + (longitudeTileNumber + j - 1) + "/" + (latitudeTileNumber + i - 1) + ".png"));
                    Bitmap bmImg = BitmapFactory.decodeStream(url.openStream());
                    cache.add(tileCoord);
                    maps[j][i] = bmImg;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            coord = getPositionOnTile(lat, lon, zoom);
        }
        return new Map(maps, coord);
    }
