    private Document loadDocument(URL url, String signatureResourceName) throws IOException, ParserConfigurationException, SAXException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(url.openStream(), "UTF8", true, true);
        ZipArchiveEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextZipEntry())) {
            if (false == signatureResourceName.equals(zipEntry.getName())) {
                continue;
            }
            Document document = loadDocument(zipInputStream);
            return document;
        }
        return null;
    }
