    public Map<String, String> fetchUrl(String url, HttpMethod method, Map<String, String> params, ContentType contentType, Map<String, String> headers) {
        ValidatorUtils.validateNullOrEmpty(url, method);
        Map<String, String> responseParams = null;
        try {
            String queryString = null;
            if (params != null) queryString = MapUtils.mapToQueryStringEncoded(params);
            if (method == HttpMethod.GET && queryString != null) url += "?" + queryString;
            URL _url = new URL(url);
            URLConnection conn = _url.openConnection();
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType.getContentType());
                conn.setRequestProperty("Accept", contentType.getContentType());
            }
            if (headers != null) {
                for (Entry<String, String> e : headers.entrySet()) {
                    conn.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            if (method == HttpMethod.POST && queryString != null) {
                setParamsByPost(conn, queryString);
                conn.setDoOutput(true);
            }
            responseParams = parseResponse(conn, contentType);
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error when fetching url", e);
        }
        return responseParams;
    }
