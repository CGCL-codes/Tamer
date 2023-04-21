    public CalaisResponse analyze(URL url, CalaisConfig config) throws IOException {
        config.set(UserParam.EXTERNAL_ID, url.toString());
        config.set(ProcessingParam.CONTENT_TYPE, "TEXT/HTML");
        return analyze(new InputStreamReader(url.openStream()), config);
    }
