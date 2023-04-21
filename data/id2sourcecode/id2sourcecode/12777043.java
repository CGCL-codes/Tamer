    private void run(final Workbook pWorkbook, final String pOutputFile, String pTemplate) throws IOException, XMLStreamException {
        InputStream istream = null;
        try {
            final URL url = getClass().getResource(pTemplate);
            istream = url.openStream();
            final DocumentParser parser = new DocumentParser();
            parser.process(pWorkbook, new StreamSource(url.openStream()));
            istream.close();
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (Throwable t) {
                }
            }
        }
        OutputStream ostream = null;
        try {
            istream = null;
            ostream = new FileOutputStream(pOutputFile);
            pWorkbook.write(ostream);
            ostream.close();
            ostream = null;
        } finally {
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (Throwable t) {
                }
            }
        }
    }
