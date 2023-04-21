    private InputStream getInputStream(String url) throws IOException {
        if (url.startsWith("file://")) {
            File file = new File(url.substring("file://".length()));
            return new FileInputStream(file);
        } else {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new IOException("Found no file at " + url);
                }
                return new BufferedInputStream(entity.getContent(), 64 * 1024);
            }
            throw new IOException("Could not get the file '" + url + "'. URL returned: " + response.getStatusLine());
        }
    }
