    protected void closeExportFile(Element exportNode) throws IOException, SAXException {
        getSaxWriter().writeClose(exportNode);
        CmsXmlSaxWriter xmlSaxWriter = (CmsXmlSaxWriter) getSaxWriter().getContentHandler();
        xmlSaxWriter.endDocument();
        ZipEntry entry = new ZipEntry(CmsImportExportManager.EXPORT_MANIFEST);
        getExportZipStream().putNextEntry(entry);
        StringBuffer result = ((StringWriter) xmlSaxWriter.getWriter()).getBuffer();
        int steps = result.length() / SUB_LENGTH;
        int rest = result.length() % SUB_LENGTH;
        int pos = 0;
        for (int i = 0; i < steps; i++) {
            String sub = result.substring(pos, pos + SUB_LENGTH);
            getExportZipStream().write(sub.getBytes(OpenCms.getSystemInfo().getDefaultEncoding()));
            pos += SUB_LENGTH;
        }
        if (rest > 0) {
            String sub = result.substring(pos, pos + rest);
            getExportZipStream().write(sub.getBytes(OpenCms.getSystemInfo().getDefaultEncoding()));
        }
        getExportZipStream().closeEntry();
        getExportZipStream().close();
    }
