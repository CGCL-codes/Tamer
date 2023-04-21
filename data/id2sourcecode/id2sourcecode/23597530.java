    @Override
    public void downloadResourcesForTemplate(long templateOid) {
        PathConfig pathConfig = PathConfig.getInstance(configuration);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(configuration.getBeehiveRESTRootUrl() + "account/" + userService.getAccount().getOid() + "/template/" + templateOid + "/resource");
        InputStream inputStream = null;
        FileOutputStream fos = null;
        this.addAuthentication(httpGet);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (200 == response.getStatusLine().getStatusCode()) {
                inputStream = response.getEntity().getContent();
                File userFolder = new File(pathConfig.userFolder(userService.getAccount()));
                if (!userFolder.exists()) {
                    boolean success = userFolder.mkdirs();
                    if (!success) {
                        throw new FileOperationException("Unable to create directories for path '" + userFolder + "'.");
                    }
                }
                File outPut = new File(userFolder, "template.zip");
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
            } else if (404 == response.getStatusLine().getStatusCode()) {
                LOGGER.warn("There are no resources for this template, ID:" + templateOid);
                return;
            } else {
                throw new BeehiveNotAvailableException("Failed to download resources for template, status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException ioException) {
            throw new BeehiveNotAvailableException("I/O exception in handling user's template.zip file: " + ioException.getMessage(), ioException);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    LOGGER.warn("Failed to close input stream from '" + httpGet.getURI() + "': " + ioException.getMessage(), ioException);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioException) {
                    LOGGER.warn("Failed to close file output stream to user's template.zip file: " + ioException.getMessage(), ioException);
                }
            }
        }
    }
