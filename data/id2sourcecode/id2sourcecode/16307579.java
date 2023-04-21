    public List parse(URL url) throws IOException, CsvParserException {
        logger.debug("parse(url={}) - start", url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            return parse(reader, url.toString());
        } finally {
            reader.close();
        }
    }
