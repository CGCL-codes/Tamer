    public void writeInputXml(Writer writer, File inputDocFile, String pathForXml, String xmlFileName, String packageName) throws IOException, InputXMLAlreadyExistsException {
        String xmlInSeparatedFile = wconf.xmlInSeparatedFileElementValue;
        try {
            DocumentSource xmlInputDoc = new DocumentSource(new SAXReader().read(inputDocFile));
            Document xmlDocument = xmlInputDoc.getDocument();
            if ("yes".equals(xmlInSeparatedFile)) {
                writeSeparatedInputXml(pathForXml, xmlFileName, xmlDocument, writer, packageName);
            } else if ("no".equals(xmlInSeparatedFile)) {
                writeInsideInputXml(xmlDocument, writer);
            }
        } catch (DocumentException e) {
            RuntimeException rte = new RuntimeException("NO xml document!");
            rte.initCause(e);
        }
    }
