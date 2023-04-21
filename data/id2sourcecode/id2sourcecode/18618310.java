    private InputStream openStream() throws LoadException {
        InputStream is;
        String file = getResource();
        if (getResourceType() == FILE) {
            try {
                File fd = new File(file);
                is = new MeteredStream(new FileInputStream(fd));
                setLength((int) fd.length());
            } catch (Exception ioe) {
                throw new LoadException("File " + file + " could not be opened: " + ioe.getMessage(), LoadException.CDTPARSE);
            }
        } else {
            try {
                java.net.URL url = new java.net.URL(file);
                java.net.URLConnection conn = url.openConnection();
                is = new MeteredStream(conn.getInputStream());
                setLength(conn.getContentLength());
            } catch (IOException ioe2) {
                throw new LoadException("Url " + file + " could not be opened: " + ioe2.getMessage(), LoadException.CDTPARSE);
            }
        }
        return is;
    }
