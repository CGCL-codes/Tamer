    public MappingFileData read(URL url) {
        MappingFileData result = null;
        InputStream stream = null;
        try {
            stream = url.openStream();
            Document document = documentBuilder.parse(stream);
            MappingsSource parser = new XMLParser(document);
            result = parser.load();
        } catch (Throwable e) {
            log.error("Error while loading dozer mapping file url: [" + url + "]", e);
            MappingUtils.throwMappingException(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                MappingUtils.throwMappingException(e);
            }
        }
        return result;
    }
