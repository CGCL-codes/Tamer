    private Document download(String urlS) throws IOException {
        URL url = new URL(urlS);
        Document document = null;
        try {
            document = AbstractLegacyConverter.htmlTidy(url.openStream());
        } catch (Exception e) {
            throw new CMLRuntimeException("parse: " + e);
        }
        return document;
    }
