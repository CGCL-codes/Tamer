    public DefaultTileSource(final URL url) throws IOException, TileCodeFormatException {
        final Properties tileList = new Properties();
        tileList.load(new InputStreamReader(url.openStream()));
        loadTiles(tileList);
    }
