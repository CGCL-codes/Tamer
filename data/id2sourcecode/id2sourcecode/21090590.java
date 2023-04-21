    private SFSInputStream makeSfsStream(URL url) throws IOException {
        URLConnection xmlConn = url.openConnection();
        return new SFSInputStream(xmlConn.getInputStream(), xmlConn.getContentLength());
    }
