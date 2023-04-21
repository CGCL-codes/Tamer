    private void addManifestEntryToZip(ZipOutputStream zipOutStream, Document document) throws IOException, TransformerFactoryConfigurationError, TransformerException {
        zipOutStream.putNextEntry(new ZipEntry(IMSMANIFEST_FILE_NAME));
        JCRXMLConverter.writeXmlDocument(document, zipOutStream);
        zipOutStream.closeEntry();
    }
