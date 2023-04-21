    protected void loadMapTiles() {
        long count1 = 0;
        for (int zoom = minZoom; zoom <= maxZoom; zoom++) {
            int tileSize = getTileFactory().getTileSize();
            Point2D topLeftPixel = getTileFactory().geoToPixel(topLeft, zoom);
            Point2D bottomRightPixel = getTileFactory().geoToPixel(bottomRight, zoom);
            int topLeftX = (int) Math.floor(topLeftPixel.getX() / tileSize);
            int topLeftY = (int) Math.floor(topLeftPixel.getY() / tileSize);
            int bottomRightX = (int) Math.floor(bottomRightPixel.getX() / tileSize);
            int bottomRightY = (int) Math.floor(bottomRightPixel.getY() / tileSize);
            for (int x = topLeftX; x <= bottomRightX; x++) {
                for (int y = topLeftY; y <= bottomRightY; y++) {
                    count1++;
                }
            }
        }
        monitor = new ProgressMonitor(null, "Downloading", "", 0, (int) count1);
        long count2 = 0;
        for (int zoom = minZoom; zoom <= maxZoom; zoom++) {
            int tileSize = getTileFactory().getTileSize();
            Point2D topLeftPixel = getTileFactory().geoToPixel(topLeft, zoom);
            Point2D bottomRightPixel = getTileFactory().geoToPixel(bottomRight, zoom);
            int topLeftX = (int) Math.floor(topLeftPixel.getX() / tileSize);
            int topLeftY = (int) Math.floor(topLeftPixel.getY() / tileSize);
            int bottomRightX = (int) Math.floor(bottomRightPixel.getX() / tileSize);
            int bottomRightY = (int) Math.floor(bottomRightPixel.getY() / tileSize);
            for (int x = topLeftX; x <= bottomRightX; x++) {
                for (int y = topLeftY; y <= bottomRightY; y++) {
                    try {
                        String pathName = new StringBuffer().append(zoom).append("/").append(x).toString();
                        File path = new File(pathName);
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                        String fileName = new StringBuffer().append(y).append(".png").toString();
                        File file = new File(pathName, fileName);
                        if (!file.exists()) {
                            URL url = new URL(getTileFactory().getInfo().getTileUrl(x, y, zoom));
                            BufferedImage image = ImageIO.read(url.openStream());
                            if (image != null) {
                                try {
                                    ImageIO.write(image, "png", file);
                                } catch (IOException e) {
                                    System.err.println("Could not write image " + pathName + " because:");
                                    e.printStackTrace();
                                }
                            }
                        }
                        count2++;
                        monitor.setNote("Downloading tile " + count2 + " of " + count1);
                        monitor.setProgress((int) count2);
                    } catch (Exception e) {
                    }
                }
            }
        }
        System.out.println("Loaded " + count1 + " tiles.");
    }
