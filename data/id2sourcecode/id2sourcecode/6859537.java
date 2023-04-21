    public boolean parseResults(URL url, String data_type, CurationI curation, Date analysis_date, String regexp) throws OutputMalFormatException {
        boolean parsed = false;
        try {
            InputStream data_stream = url.openStream();
            parsed = parseResults(data_stream, data_type, curation, analysis_date, regexp);
            data_stream.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parsed;
    }
