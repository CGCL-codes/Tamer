    public void configure(URL url) throws CrudeliaException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            createConfig(url.openStream(), url.toString());
        } catch (IOException ioe) {
            throw new CrudeliaException("could not configure from URL: " + url, ioe);
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }
