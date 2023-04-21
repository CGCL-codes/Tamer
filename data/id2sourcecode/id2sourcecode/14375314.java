    private HttpClientResponse execute(HttpRequestBase request) throws HttpProcessException {
        try {
            request.addHeader("User-Agent", createUserAgentHeaderValue());
            final HttpResponse apacheResponse = apacheClient.execute(request);
            return new HttpClientResponseAdapter(apacheResponse);
        } catch (ClientProtocolException e) {
            throw new HttpProtocolException(e);
        } catch (IOException e) {
            throw new HttpProtocolException(ManagedIOException.manage(e));
        }
    }
