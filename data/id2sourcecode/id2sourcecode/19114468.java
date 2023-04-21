    protected String slurpUrl(URL url) {
        try {
            return slurpStream(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
