    public URLOutputStream(URL url) throws IOException, NullPointerException {
        super();
        URLConnection urlc;
        String protocol;
        if (url == null) {
            throw new NullPointerException("url");
        }
        protocol = url.getProtocol();
        if (protocol.equals("file")) {
            os_ = new FileOutputStream(url.getPath(), true);
        } else if (protocol.equals("socket")) {
            os_ = new Socket(url.getHost(), url.getPort()).getOutputStream();
        } else {
            urlc = new java.net.URL(protocol, url.getHost(), url.getPort(), url.getPath()).openConnection();
            urlc.setDoOutput(true);
            os_ = urlc.getOutputStream();
        }
    }
