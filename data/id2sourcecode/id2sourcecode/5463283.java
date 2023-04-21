    public static String getCloudmadeToken() {
        if (mToken.length() == 0) {
            synchronized (mToken) {
                if (mToken.length() == 0) {
                    final String url = "http://auth.cloudmade.com/token/" + mKey + "?userid=" + mAndroidId;
                    final HttpClient httpClient = new DefaultHttpClient();
                    final HttpPost httpPost = new HttpPost(url);
                    try {
                        final HttpResponse response = httpClient.execute(httpPost);
                        if (DEBUGMODE) {
                            logger.debug("Response from Cloudmade auth: " + response.getStatusLine());
                        }
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            final BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), StreamUtils.IO_BUFFER_SIZE);
                            final String line = br.readLine();
                            if (DEBUGMODE) {
                                logger.debug("First line from Cloudmade auth: " + line);
                            }
                            mToken = line.trim();
                            if (mToken.length() > 0) {
                                mPreferenceEditor.putString(CLOUDMADE_TOKEN, mToken);
                                mPreferenceEditor.commit();
                                mPreferenceEditor = null;
                            } else {
                                logger.error("No authorization token received from Cloudmade");
                            }
                        }
                    } catch (final IOException e) {
                        logger.error("No authorization token received from Cloudmade: " + e);
                    }
                }
            }
        }
        return mToken;
    }
