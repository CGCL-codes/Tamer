        public void run() {
            final Tile tile = tileQueue.remove();
            final URL url = mapInfo.getTileUrl(tile);
            try {
                final InputStream stream = url.openStream();
                final ImageData[] data = new ImageLoader().load(stream);
                final Image image = new Image(Display.getDefault(), data[0]);
                tile.setImage(image);
            } catch (final Exception e) {
                tile.setError(e);
            } finally {
                loadingTiles.remove(url);
            }
        }
