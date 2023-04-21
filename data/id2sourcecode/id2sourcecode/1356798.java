    @Override
    public String shortURL(String url) throws NoSuchAlgorithmException, IOException {
        HttpTransport transport = new NetHttpTransport();
        GoogleHeaders headers = new GoogleHeaders();
        headers.setApplicationName("Hirubila/1.0");
        headers.put("Content-Type", "application/json");
        transport.defaultHeaders = headers;
        JsonHttpParser parser = new JsonHttpParser();
        parser.jsonFactory = new JacksonFactory();
        transport.addParser(parser);
        HttpRequest request = transport.buildPostRequest();
        request.setUrl(GOOGL_URL);
        GenericData data = new GenericData();
        data.put("longUrl", url);
        JsonHttpContent content = new JsonHttpContent();
        content.data = data;
        content.jsonFactory = parser.jsonFactory;
        request.content = content;
        HttpResponse response = request.execute();
        Result result = response.parseAs(Result.class);
        return result.id;
    }
