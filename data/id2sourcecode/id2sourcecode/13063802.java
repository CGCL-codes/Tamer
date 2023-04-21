    @Override
    public String getResponseText(final PageRequestResponse requestResponse) {
        final String url = getRequestUrl(requestResponse);
        final HttpClient client = new DefaultHttpClient();
        final HttpGet get = new HttpGet(url);
        try {
            final HttpResponse response = client.execute(get);
            final InputStream inputStream = response.getEntity().getContent();
            try {
                return IoUtils.getStringFromInputStream(inputStream, PageUtils.pageConfig.getCharset());
            } finally {
                inputStream.close();
            }
        } catch (final IOException ex) {
            get.abort();
            throw convertRuntimeException(ex, url);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
