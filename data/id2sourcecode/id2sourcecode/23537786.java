    public List<SyndEntry> requestFeed(String url) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpGet httpGet = new HttpGet(url);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            httpGet.setHeader("If-Modified-Since", sdf.format(this.getLastUpdated()));
            logger.fine("Executing request " + httpGet.getRequestLine());
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.fine("Received " + httpResponse.getStatusLine());
            XmlReader reader = new XmlReader(httpResponse.getEntity().getContent());
            SyndFeed feed = this.input.build(reader);
            return feed.getEntries();
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return null;
        }
    }
