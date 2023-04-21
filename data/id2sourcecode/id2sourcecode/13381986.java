    public Configuration configure(URL url) throws CrossException {
        try {
            return doConfigure(url.openStream(), url.toString());
        } catch (IOException ioe) {
            throw new CrossException("could not configure from URL: " + url, ioe);
        }
    }
