    @Override
    protected Movie doInBackground(String... params) {
        try {
            String imdbId = params[0];
            HttpClient httpClient = MyMovies.getHttpClient();
            String format = parserKind == PARSER_KIND_JSON ? "json" : "xml";
            String path = "/Movie.imdbLookup/en/" + format + "/" + API_KEY + "/" + imdbId;
            HttpGet request = new HttpGet(API_ENDPOINT + path);
            HttpResponse response = httpClient.execute(request);
            InputStream data = response.getEntity().getContent();
            switch(parserKind) {
                case PARSER_KIND_SAX:
                    return SAXMovieParser.parseMovie(data);
                case PARSER_KIND_XMLPULL:
                    return XmlPullMovieParser.parseMovie(data);
                case PARSER_KIND_JSON:
                    return JsonMovieParser.parseMovie(data);
                default:
                    throw new RuntimeException("unsupported parser");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
