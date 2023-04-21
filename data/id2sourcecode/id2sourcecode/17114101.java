    private HttpResponse executeHttp(HttpGet httpGet) {
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            return response;
        } catch (Throwable e) {
            throw new HttpTestClientException(e);
        }
    }
