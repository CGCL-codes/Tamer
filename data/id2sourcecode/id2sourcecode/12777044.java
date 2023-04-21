    private void run(final String pTemplateSheet, String pTemplate) throws IOException, XMLStreamException {
        final URL url = getClass().getResource(pTemplateSheet);
        InputStream istream = null;
        final Workbook workbook;
        try {
            istream = url.openStream();
            workbook = new HSSFWorkbook(istream, true);
            istream = null;
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (Throwable t) {
                }
            }
        }
        final String outputFile = "target/" + pTemplateSheet;
        run(workbook, outputFile, pTemplate);
    }
