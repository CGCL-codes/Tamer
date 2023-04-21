    public TileSet readTileset(String filename) throws Exception {
        String xmlFile = filename;
        xmlPath = filename.substring(0, filename.lastIndexOf(File.separatorChar) + 1);
        xmlFile = makeUrl(xmlFile);
        xmlPath = makeUrl(xmlPath);
        URL url = new URL(xmlFile);
        return unmarshalTilesetFile(url.openStream(), filename);
    }
