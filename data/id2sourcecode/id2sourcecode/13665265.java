    public MidhedavaMapStructure readMap(String filename) throws Exception {
        xmlPath = filename.substring(0, filename.lastIndexOf(File.separatorChar) + 1);
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            String xmlFile = makeUrl(filename);
            URL url = new URL(xmlFile);
            is = url.openStream();
        }
        if (is == null) {
            return null;
        }
        if (filename.endsWith(".gz")) {
            is = new GZIPInputStream(is);
        }
        MidhedavaMapStructure unmarshalledMap = unmarshal(is);
        unmarshalledMap.setFilename(filename);
        return unmarshalledMap;
    }
