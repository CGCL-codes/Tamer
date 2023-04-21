    private DefaultProcessorURI setupURI(String url) throws MalformedURLException, IOException {
        UURI uuri = UURIFactory.getInstance(url);
        DefaultProcessorURI curi = new DefaultProcessorURI(uuri, uuri, LinkContext.NAVLINK_MISC);
        URLConnection conn = new URL(url).openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        InputStream in = conn.getInputStream();
        Recorder recorder = Recorder.wrapInputStreamWithHttpRecord(this.getTmpDir(), this.getClass().getName(), in, null);
        logger.info("got recorder for " + url);
        curi.setContentSize(recorder.getRecordedInput().getSize());
        curi.setContentType("application/x-shockwave-flash");
        curi.setFetchStatus(200);
        curi.setRecorder(recorder);
        return curi;
    }
