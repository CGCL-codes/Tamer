    public static Document parseXml(URL url) throws ExtractaException {
        try {
            return parseXml(new BufferedInputStream(url.openStream()));
        } catch (IOException ex) {
            throw new ExtractaException("Error opening URL.", ex);
        }
    }
