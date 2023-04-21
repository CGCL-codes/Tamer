    public void downloadTo(File destination) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        if (basicAuthToken != null) {
            log.info("Using basic authentication");
            log.debug("username: " + basicAuthToken.getUsername());
            log.debug("password: " + basicAuthToken.getPassword());
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(basicAuthToken.getHost(), basicAuthToken.getPort()), new UsernamePasswordCredentials(basicAuthToken.getUsername(), basicAuthToken.getPassword()));
        }
        HttpGet httpResource = new HttpGet(url);
        log.info(httpResource.getRequestLine());
        HttpResponse response = httpclient.execute(httpResource);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        byte[] b = new byte[1024];
        int len;
        OutputStream out = new FileOutputStream(destination);
        while ((len = in.read(b)) != -1) out.write(b, 0, len);
        log.info(response.getStatusLine());
        if (entity != null) {
            contentLength = entity.getContentLength();
            log.info(entity.getContentType());
            log.info("Content-length: " + entity.getContentLength());
            entity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
        log.info("Download saved to " + destination.getAbsolutePath());
    }
