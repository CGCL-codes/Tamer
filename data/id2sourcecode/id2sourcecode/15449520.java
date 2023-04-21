    public File doGetFile(String url, File inFile) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-start  #####, url=" + url);
        }
        InputStream responseBodyInputStream = null;
        GetMethod gMethod = new GetMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            gMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            this.hc.executeMethod(gMethod);
            responseBodyInputStream = gMethod.getResponseBodyAsStream();
            final int bufferSize = 2048;
            FileOutputStream fout = new FileOutputStream(inFile);
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            while ((readCount = responseBodyInputStream.read(buffer)) != -1) {
                if (readCount < bufferSize) {
                    fout.write(buffer, 0, readCount);
                } else {
                    fout.write(buffer);
                }
            }
            fout.close();
        } finally {
            gMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-end    #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=[InputStream]\n");
        }
        return inFile;
    }
