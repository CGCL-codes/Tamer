    private void downloadOpenRemoteZip() throws IOException {
        PathConfig pathConfig = PathConfig.getInstance(configuration);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(configuration.getBeehiveRESTRootUrl() + "user/" + userService.getCurrentUser().getUsername() + "/openremote.zip");
        LOGGER.debug("Attempting to fetch account configuration from: " + httpGet.getURI());
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            this.addAuthentication(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            if (HttpServletResponse.SC_NOT_FOUND == response.getStatusLine().getStatusCode()) {
                LOGGER.warn("Failed to download openremote.zip for user " + userService.getCurrentUser().getUsername() + " from Beehive. Status code is 404. Will try to restore panels from local resource! ");
                return;
            }
            if (200 == response.getStatusLine().getStatusCode()) {
                inputStream = response.getEntity().getContent();
                File userFolder = new File(pathConfig.userFolder(userService.getAccount()));
                if (!userFolder.exists()) {
                    boolean success = userFolder.mkdirs();
                    if (!success) {
                        throw new FileOperationException("Failed to create the required directories for path '" + userFolder + "'.");
                    }
                }
                File outPut = new File(userFolder, "openremote.zip");
                FileUtilsExt.deleteQuietly(outPut);
                fos = new FileOutputStream(outPut);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                ZipUtils.unzip(outPut, pathConfig.userFolder(userService.getAccount()));
                FileUtilsExt.deleteQuietly(outPut);
            } else {
                throw new BeehiveNotAvailableException("Failed to download resources, status code: " + response.getStatusLine().getStatusCode());
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    LOGGER.warn("Failed to close input stream from " + httpGet.getURI() + " (" + ioException.getMessage() + ")", ioException);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioException) {
                    LOGGER.warn("Failed to close output stream to user's openremote.zip file: " + ioException.getMessage());
                }
            }
        }
    }
