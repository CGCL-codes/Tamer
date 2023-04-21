    private void streamZip(Process process, String tags, String title, OutputStream out) throws IOException, XMLException, ParserConfigurationException {
        ZipOutputStream zipOut = new ZipOutputStream(out);
        zipOut.putNextEntry(new ZipEntry("process.xml"));
        Document doc = process.getRootOperator().getDOMRepresentation();
        XMLTools.setTagContents(doc.getDocumentElement(), "title", title);
        zipOut.write(XMLTools.toString(doc, XMLImporter.PROCESS_FILE_CHARSET).getBytes(XMLImporter.PROCESS_FILE_CHARSET));
        zipOut.closeEntry();
        zipOut.putNextEntry(new ZipEntry("preview.png"));
        streamPreviewImage(process, zipOut);
        zipOut.closeEntry();
        zipOut.putNextEntry(new ZipEntry("preview.svg"));
        zipOut.write(makeSVG(process));
        zipOut.closeEntry();
        zipOut.putNextEntry(new ZipEntry("metadata.xml"));
        XMLTools.stream(makeMetaData(process, tags), zipOut, XMLImporter.PROCESS_FILE_CHARSET);
        zipOut.closeEntry();
        zipOut.finish();
    }
