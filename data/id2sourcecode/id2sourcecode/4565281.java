    protected ResultSet executeScan(String startTerm, int maxResults, String fieldName, String indexBase, String indexName) throws GenericSearchException {
        ResultSet rs = null;
        URL url = null;
        String st = startTerm;
        if (st == null || st.trim().equals("")) st = "0";
        try {
            url = new URL(indexBase + scanParams + "&maximumTerms=" + maxResults + "&scanClause=" + URLEncoder.encode(fieldName + "=" + st.trim(), "UTF-8"));
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
