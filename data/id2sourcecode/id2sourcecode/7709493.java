    public static Document parseHtml(URL url) throws ExtractaException {
        try {
            return parseHtml(new BufferedInputStream(url.openStream()));
        } catch (IOException ex) {
            throw new ExtractaException("Error opening URL.", ex);
        }
    }
