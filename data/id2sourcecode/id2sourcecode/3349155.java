    public TracDataSource(String urlString) throws IOException, ValidityException, ParsingException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Builder parser = new Builder();
        Document doc = parser.build(conn.getInputStream());
        this.seriesGroups = new ArrayList<SeriesGroup>();
        this.parseXml(doc);
    }
