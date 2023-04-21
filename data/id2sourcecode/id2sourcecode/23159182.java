    public void save() throws IOException {
        if (validateData()) {
            final ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(_file));
            final Writer writer = new OutputStreamWriter(zipOut, Utilities.DEFAULT_CHARSET);
            zipOut.putNextEntry(new ZipEntry("challenge.xml"));
            XMLUtils.writeXML(_challengeDocument, writer, Utilities.DEFAULT_CHARSET.name());
            zipOut.closeEntry();
            zipOut.putNextEntry(new ZipEntry("score.xml"));
            XMLUtils.writeXML(_scoreDocument, writer, Utilities.DEFAULT_CHARSET.name());
            zipOut.closeEntry();
            zipOut.close();
        }
    }
