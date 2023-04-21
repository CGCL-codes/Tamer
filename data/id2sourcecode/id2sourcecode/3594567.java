    public Map<String, String> fetchUrl(String url, ContentType contentType) {
        ValidatorUtils.validateNullOrEmpty(url);
        Map<String, String> responseParams = null;
        try {
            URL _url = new URL(url);
            URLConnection conn = _url.openConnection();
            responseParams = parseResponse(conn, contentType);
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error when fetching url", e);
        }
        return responseParams;
    }
