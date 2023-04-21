    private synchronized File loadFile(String url, File downloadDir) throws Exception {
        File tmpFile = File.createTempFile(StringUtils.getFilename(url), null, downloadDir);
        LOGGER.info(String.format("Load file %1$s into %2$s", url, tmpFile));
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        try {
            HttpResponse httResponse = httpClient.execute(httpRequest);
            final int statusCode = httResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                FileCopyUtils.copy(new GZIPInputStream(httResponse.getEntity().getContent()), new FileOutputStream(tmpFile));
                return tmpFile;
            } else {
                LOGGER.warning("Bad response code =" + statusCode);
                throw new Exception("Can't load " + url + ". Result status code = " + statusCode);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
