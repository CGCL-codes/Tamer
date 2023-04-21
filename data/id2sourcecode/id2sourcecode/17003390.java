    public static InputStream openStream(String locationURI) throws JETException {
        try {
            URI uri = URI.createURI(locationURI);
            URL url;
            try {
                uri = CommonPlugin.resolve(uri);
                url = new URL(uri.toString());
            } catch (MalformedURLException exception) {
                url = new URL("file:" + locationURI);
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            return bufferedInputStream;
        } catch (IOException exception) {
            throw new JETException(exception.getLocalizedMessage(), exception);
        }
    }
