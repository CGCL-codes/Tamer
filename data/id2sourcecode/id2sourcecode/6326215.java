    protected InputStream getInputStream() {
        try {
            HttpUriRequest request = new HttpGet(feedUrl.toString());
            request.addHeader("Accept-Encoding", "gzip");
            request.addHeader("User-Agent", BikeRouteConsts.AGENT);
            final HttpResponse response = new DefaultHttpClient().execute(request);
            Header ce = response.getFirstHeader("Content-Encoding");
            String contentEncoding = null;
            if (ce != null) {
                contentEncoding = ce.getValue();
            }
            InputStream instream = response.getEntity().getContent();
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            }
            return instream;
        } catch (IOException e) {
            Log.e("XML parser", e.getMessage() + feedUrl);
            return null;
        }
    }
