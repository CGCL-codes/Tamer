    public void execute() throws BuildException {
        super.execute();
        if (path == null) {
            throw new BuildException("Must specify 'path' attribute");
        }
        if ((war == null) && (localWar == null) && (config == null) && (tag == null)) {
            throw new BuildException("Must specify either 'war', 'localWar', 'config', or 'tag' attribute");
        }
        BufferedInputStream stream = null;
        String contentType = null;
        int contentLength = -1;
        if (war != null) {
            if (war.startsWith("file:")) {
                try {
                    URL url = new URL(war);
                    URLConnection conn = url.openConnection();
                    contentLength = conn.getContentLength();
                    stream = new BufferedInputStream(conn.getInputStream(), 1024);
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            } else {
                try {
                    stream = new BufferedInputStream(new FileInputStream(war), 1024);
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
            contentType = "application/octet-stream";
        }
        StringBuffer sb = new StringBuffer("/deploy?path=");
        try {
            sb.append(URLEncoder.encode(this.path, getCharset()));
            if ((war == null) && (config != null)) {
                sb.append("&config=");
                sb.append(URLEncoder.encode(config, getCharset()));
            }
            if ((war == null) && (localWar != null)) {
                sb.append("&war=");
                sb.append(URLEncoder.encode(localWar, getCharset()));
            }
            if (update) {
                sb.append("&update=true");
            }
            if (tag != null) {
                sb.append("&tag=");
                sb.append(URLEncoder.encode(tag, getCharset()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new BuildException("Invalid 'charset' attribute: " + getCharset());
        }
        execute(sb.toString(), stream, contentType, contentLength);
    }
