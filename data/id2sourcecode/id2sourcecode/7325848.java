    public InputStream getStream(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            return response.getEntity().getContent();
        } catch (URISyntaxException e) {
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }
