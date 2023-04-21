        public Tile(final int xTile, final int yTile, final int zoom) throws IOException {
            File cacheFile = new File(getTileDirName() + zoom + File.separator + xTile + File.separator + yTile + ".png");
            if (cacheFile.exists() && cacheFile.length() > 0) {
                try {
                    this.myBitmap = javax.imageio.ImageIO.read(cacheFile);
                } catch (IIOException e) {
                    LOG.log(Level.WARNING, "cannot load tile from cached file " + cacheFile.getCanonicalPath(), e);
                }
            }
            if (this.myBitmap == null || this.myBitmap.getWidth() < 1 || this.myBitmap.getHeight() < 1) {
                if (yTile < 0 || xTile < 0 || yTile >= 1 << zoom || xTile >= 1 << zoom) throw new IOException("Tile out of range! zoom=" + zoom + " x=" + xTile + " y=" + yTile);
                URL url = new URL("http://tile.openstreetmap.org/" + zoom + "/" + xTile + "/" + yTile + ".png");
                try {
                    URLConnection connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "osmnavigation TilePaintVisitor/" + OsmNavigationConfigSection.getVersion());
                    myBitmap = javax.imageio.ImageIO.read(connection.getInputStream());
                } catch (IIOException e) {
                    if (e.getCause() != null && e.getCause() instanceof UnknownHostException) {
                        LOG.log(Level.WARNING, "UnknownHostException, cannot download tile " + url.toExternalForm());
                    } else {
                        LOG.log(Level.WARNING, "cannot download tile " + url.toExternalForm(), e);
                    }
                }
                LOG.log(Level.FINE, "new Tile downloaded");
                if (this.myBitmap != null) try {
                    cacheFile.getParentFile().mkdirs();
                    javax.imageio.ImageIO.write(this.myBitmap, "png", cacheFile);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "cannot save tile to cached file " + cacheFile.getCanonicalPath(), e);
                }
            }
            this.myXTileNumber = xTile;
            this.myYTileNumber = yTile;
            this.myZoomLevel = zoom;
            projected = projectTileNumber(myXTileNumber, myYTileNumber, zoom);
        }
