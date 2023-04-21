    public Document loadDocumentFromZipEntry(URL url, String entryName) throws XMLLoadingException {
        Document doc = null;
        InputStream inStream;
        ZipEntry entry = null, xmlEntry = null;
        ZipInputStream zipInStream;
        try {
            inStream = url.openStream();
        } catch (IOException e) {
            throw new XMLLoadingException("Problem opening stream from url '" + url + "'", e);
        }
        try {
            zipInStream = new ZipInputStream(inStream);
            do {
                entry = zipInStream.getNextEntry();
                if (entryName.equals(entry.getName())) {
                    xmlEntry = entry;
                }
            } while ((entry != null) && (xmlEntry == null));
        } catch (IOException e) {
            throw new XMLLoadingException("Problem fetching next zip entry in stream from url '" + url + "'", e);
        }
        if (xmlEntry == null) {
            throw new XMLLoadingException("Couldn't find entry '" + entryName + "' in file at url '" + url + "'");
        }
        try {
            doc = loadDocumentFromStream(zipInStream);
        } catch (XMLLoadingException e) {
            throw new LocalFileXMLLoadingException("Problem occured loading document from zip url '" + url + "'", e);
        }
        return doc;
    }
