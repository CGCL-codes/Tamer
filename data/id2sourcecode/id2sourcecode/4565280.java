    protected ResultSet executeQuery(String query, int startRecord, int maxResults, String indexBase, String indexName) throws GenericSearchException {
        ResultSet rs = null;
        URL url = null;
        try {
            url = new URL(indexBase + queryParams + "&startRecord=" + startRecord + "&maximumRecords=" + maxResults + "&query=" + URLEncoder.encode(query, "UTF-8"));
        } catch (MalformedURLException e) {
            throw new GenericSearchException(e.toString());
        } catch (UnsupportedEncodingException e) {
            throw new GenericSearchException(e.toString());
        }
        if (logger.isDebugEnabled()) logger.debug("url=" + url);
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            conn.connect();
        } catch (IOException e) {
            throw new GenericSearchException(e.toString());
        }
        content = null;
        try {
            content = conn.getContent();
        } catch (IOException e) {
            throw new GenericSearchException(e.toString());
        }
        rs = new ResultSet((InputStream) content);
        return rs;
    }
