    protected Workbook getWorkbook() throws IOException {
        log.debug("getWorkbook: ->");
        final DocumentProcessor.Type type = getSpreadsheetType() == null ? DocumentProcessor.Type.xls : getSpreadsheetType();
        final File file = getSpreadsheetFile();
        final URL url = getSpreadsheetUrl();
        InputStream stream = null;
        final Workbook result;
        try {
            if (file == null) {
                if (url != null) {
                    log.debug("Opening URL " + url);
                    stream = url.openStream();
                }
            } else {
                log.debug("Opening file " + file);
                stream = new FileInputStream(file);
            }
            switch(type) {
                case xls:
                    result = stream == null ? new HSSFWorkbook() : new HSSFWorkbook(stream, true);
                    break;
                case xlsx:
                    result = stream == null ? new XSSFWorkbook() : new XSSFWorkbook(stream);
                    break;
                default:
                    throw new IllegalStateException("Invalid spreadsheet type: " + type);
            }
            if (stream != null) {
                stream.close();
                stream = null;
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {
                }
            }
        }
        log.debug("getWorkbook: <- " + result);
        return result;
    }
