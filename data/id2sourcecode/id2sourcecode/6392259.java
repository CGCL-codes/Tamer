    public InputStream doGet(String url, String referer) {
        HttpGet get = null;
        try {
            get = new HttpGet(url);
            setHeaders(get, referer);
            HttpResponse response = execute(get, context);
            updateCurrentUrl(get);
            if (response.getStatusLine().getStatusCode() == 302) {
                return doGet(getRedirectHandler().getLocationURI(response, context).toString(), referer);
            } else {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            throw new CustomRuntimeException();
        } finally {
            getConnectionManager().closeExpiredConnections();
        }
    }
