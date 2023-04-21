    public byte[] getResourceProxy(URL url, ProxyDefintion proxyDef) throws IOException {
        log.debug(".getResource() with Proxy " + proxyDef.toString() + ": " + url);
        Authenticator.setDefault(new SimpleAuthenticator(proxyDef.getUser(), proxyDef.getPassword()));
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("http.proxyHost", proxyDef.getProxy());
        systemProperties.setProperty("http.proxyPort", proxyDef.getPort());
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        InputStream in = httpConnection.getInputStream();
        byte[] buffer = new byte[1024];
        int bytes_read;
        ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
        while ((bytes_read = in.read(buffer)) != -1) {
            bufferOut.write(buffer, 0, bytes_read);
        }
        byte[] sresponse = bufferOut.toByteArray();
        httpConnection.disconnect();
        return sresponse;
    }
