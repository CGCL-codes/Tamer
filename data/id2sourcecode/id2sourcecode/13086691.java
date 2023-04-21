    public void setUrl(final URL url) {
        this.url = url;
        try {
            URLConnection conn = url.openConnection();
            editTime = conn.getLastModified();
        } catch (final IOException ioex) {
            ioex.printStackTrace();
        }
    }
