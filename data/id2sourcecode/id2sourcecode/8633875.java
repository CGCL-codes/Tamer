    public static InputStream getUrlStream(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                throw new IOException(status.toString());
            }
            HttpEntity entity = response.getEntity();
            return new BufferedHttpEntity(entity).getContent();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }
