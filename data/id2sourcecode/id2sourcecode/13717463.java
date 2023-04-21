    private InputStream getCDDADir() throws Exception {
        URL url = new URL("cdda:/dev/cdrom");
        return url.openStream();
    }
