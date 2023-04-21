    private void downloadTile(String tileUrl, int tileNumber) {
        URL url = null;
        InputStream in = null;
        System.out.println("Downloading tile: " + Integer.toString(tileNumber));
        try {
            url = new URL(tileUrl + ".png");
        } catch (MalformedURLException e1) {
            System.err.println("missing tile: " + tileUrl + ".png");
            System.err.println("creating blank tile instead.");
            createBlankTile("map/" + mapTitel, Integer.toString(tileNumber));
            return;
        }
        try {
            in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            RandomAccessFile file;
            file = new RandomAccessFile("map/" + mapTitel + "/" + Integer.toString(tileNumber) + ".png", "rw");
            byte[] buffer = new byte[4096];
            for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) ;
            file.write(out.toByteArray());
            file.close();
        } catch (IOException e) {
            System.err.println("cout not write tile: " + "map/" + mapTitel + "/" + Integer.toString(tileNumber) + ".png");
            System.err.println("creating blank tile instead.");
            createBlankTile("map/" + mapTitel, Integer.toString(tileNumber));
            return;
        }
    }
