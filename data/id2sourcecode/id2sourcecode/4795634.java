    protected void setUp() throws Exception {
        super.setUp();
        this.extractor = createExtractor();
        final boolean USE_NET = false;
        URL url = null;
        if (USE_NET) {
            url = new URL("http://" + this.ARCHIVE_DOT_ORG);
        } else {
            File f = new File(getTmpDir(), this.ARCHIVE_DOT_ORG + ".html");
            url = new URL("file://" + f.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(("<html><head><title>test</title><body>" + "<a href=" + this.LINK_TO_FIND + ">Hewlett Foundation</a>" + "</body></html>").getBytes());
            fos.flush();
            fos.close();
        }
        this.recorder = HttpRecorder.wrapInputStreamWithHttpRecord(getTmpDir(), this.getClass().getName(), url.openStream(), null);
    }
