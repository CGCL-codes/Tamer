    private void parseXMLMetadata(File metadataFile) {
        try {
            InputStream stream = null;
            URL url = metadataFile.toURL();
            if (url == null) {
                warn("Cannot find the metadata file : " + metadataFile.getAbsolutePath());
                m_metadata = new Element[0];
            } else {
                stream = url.openStream();
                parseXMLMetadata(stream);
            }
        } catch (MalformedURLException e) {
            error("Cannot open the metadata input stream from " + metadataFile.getAbsolutePath() + ": " + e.getMessage());
            m_metadata = null;
        } catch (IOException e) {
            error("Cannot open the metadata input stream: " + metadataFile.getAbsolutePath() + ": " + e.getMessage());
            m_metadata = null;
        }
    }
