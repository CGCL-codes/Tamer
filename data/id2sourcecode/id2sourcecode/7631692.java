    @Override
    public HttpResponse execute(HttpHost arg0, HttpRequest arg1, HttpContext arg2) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1, arg2);
    }
