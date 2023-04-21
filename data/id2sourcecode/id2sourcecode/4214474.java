    public StringBuffer get(URL url) throws IOException {
        String contents = null;
        POIFSFileSystem poifsFileSystem;
        try {
            poifsFileSystem = new POIFSFileSystem(url.openStream());
            HWPFDocument docDocument = new HWPFDocument(poifsFileSystem);
            WordExtractor wordExtractor = new WordExtractor(docDocument);
            contents = new String(wordExtractor.getText().getBytes(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StringBuffer(contents);
    }
