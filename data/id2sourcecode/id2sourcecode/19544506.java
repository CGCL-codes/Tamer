    static GenericDocumentReader<? extends GenericDocument> getReader(URL url) throws IOException {
        if ("text/xml".equals(url.openConnection().getContentType()) || "application/xml".equals(url.openConnection().getContentType())) {
            return DOMDocumentReaderFactory.getReader(implementation);
        } else if ("text/html".equals(url.openConnection().getContentType())) {
            return HTMLDocumentReaderFactory.getReader(implementation);
        }
        return null;
    }
