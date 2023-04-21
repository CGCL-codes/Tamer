    public static String getCloudmadeToken(final String aKey) throws CloudmadeException {
        final String url = "http://auth.cloudmade.com/token/" + aKey + "?userid=" + mAndroidId;
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(url);
        try {
            final HttpResponse response = httpClient.execute(httpPost);
            if (DEBUGMODE) logger.debug("Response from Cloudmade auth: " + response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                final BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8000);
                final String line = br.readLine();
                if (DEBUGMODE) logger.debug("First line from Cloudmade auth: " + line);
                final String token = line.trim();
                if (token.length() == 0) {
                    throw new CloudmadeException("No authorization token received from Cloudmade");
                }
                return token;
            }
        } catch (final IOException e) {
            throw new CloudmadeException("No authorization token received from Cloudmade", e);
        }
        throw new CloudmadeException("No authorization token received from Cloudmade");
    }
