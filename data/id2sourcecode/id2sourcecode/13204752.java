    public void displayItems() throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL(SNIPPETS_FEED + "?bq=" + URLEncoder.encode(QUERY, "UTF-8") + "&key=" + DEVELOPER_KEY);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpConnection.getInputStream();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(inputStream, new DisplayTitlesHandler());
    }
