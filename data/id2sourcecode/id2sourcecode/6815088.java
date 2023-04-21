    private boolean downloadOpenremoteZipFromBeehiveAndUnzip(String username, String password) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(PathUtil.addSlashSuffix(configuration.getBeehiveRESTRootUrl()) + "user/" + username + "/openremote.zip");
        httpGet.setHeader(Constants.HTTP_AUTHORIZATION_HEADER, Constants.HTTP_BASIC_AUTHORIZATION + encode(username, password));
        InputStream inputStream = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (200 == response.getStatusLine().getStatusCode()) {
                logger.info(httpGet.getURI() + " is available.");
                inputStream = response.getEntity().getContent();
                return writeZipAndUnzip(inputStream);
            } else if (401 == response.getStatusLine().getStatusCode()) {
                throw new ForbiddenException();
            } else if (404 == response.getStatusLine().getStatusCode()) {
                throw new ResourceNotFoundException();
            } else {
                throw new BeehiveNotAvailableException("failed to download resources for template, The status code is: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            logger.error("failed to connect to Beehive.");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("failed to close input stream while downloading " + httpGet.getURI());
                }
            }
        }
        return false;
    }
