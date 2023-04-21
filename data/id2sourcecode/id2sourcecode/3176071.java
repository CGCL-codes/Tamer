    @Override
    public boolean isCompatible(String url) throws URISyntaxException {
        URI uri = new URI(url);
        HttpClient client = new DefaultHttpClient();
        URI mangledUri = new URI(uri.getScheme() + "://" + uri.getAuthority() + "/rss.ashx");
        HttpGet get = new HttpGet(mangledUri);
        try {
            HttpResponse resp = client.execute(get);
            StatusLine respStatus = resp.getStatusLine();
            if (respStatus.getStatusCode() == HttpURLConnection.HTTP_OK) {
                return true;
            } else if (respStatus.getStatusCode() == HttpURLConnection.HTTP_UNAVAILABLE && respStatus.getReasonPhrase().equals("Rss module not active.")) {
                return false;
            } else {
                return false;
            }
        } catch (ClientProtocolException e) {
            Log.e(this.getClass().getName(), "failed checking compatibility", e);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "failed checking compatibility", e);
        } finally {
            get.abort();
        }
        return false;
    }
