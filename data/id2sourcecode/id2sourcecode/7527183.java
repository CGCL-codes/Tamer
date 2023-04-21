    @Override
    public void execute(final CustomHttpResponseHandler<TransitResponse> handler, final TransitRequest request) throws CustomHttpException {
        final String url = request.toUrl();
        Log.d("url = " + url);
        final HttpGet get = new HttpGet(url);
        HttpEntity entity = null;
        try {
            entity = mHttpClient.execute(get);
            final Reader reader = new InputStreamReader(entity.getContent(), HTTP.UTF_8);
            mParser.parse(handler, reader);
        } catch (final CustomHttpParseException ex) {
            throw new CustomHttpException(ex);
        } catch (final IOException ex) {
            throw new CustomHttpException(ex);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (final IOException ex) {
                }
            }
        }
    }
