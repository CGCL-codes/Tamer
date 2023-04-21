    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        try {
            final ZipInputStream zipStream = new ZipInputStream(url.openStream());
            DocumentSummary docSummary = new DocumentSummary();
            StringBuffer contentBuffer = new StringBuffer();
            SaxTextContentParser parser = new SaxTextContentParser();
            while (true) {
                ZipEntry entry = zipStream.getNextEntry();
                if (entry == null) {
                    break;
                }
                InputSource source = new InputSource(new InputStream() {

                    public int read() throws IOException {
                        return zipStream.read();
                    }
                });
                if (entry.getName().equals(META_FILE_NAME)) {
                    extractMetaData(source, docSummary);
                } else if (entry.getName().endsWith(CONTENT_FILE_NAME)) {
                    contentBuffer.append(parser.parse(source));
                }
                zipStream.closeEntry();
            }
            zipStream.close();
            docSummary.contentReader = new StringReader(contentBuffer.toString());
            return docSummary;
        } catch (SAXException e) {
            throw new DocumentHandlerException(GuiMessages.getString("OpenOfficeDocumentHandler.xmlParsingErrorMessage.header") + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new DocumentHandlerException(GuiMessages.getString("OpenOfficeDocumentHandler.xmlParsingErrorMessage.header") + e.getMessage(), e);
        }
    }
