    public InputStream getResourceAsStream(String arg0) {
        URL url;
        try {
            url = getResource(arg0);
            if (url != null) {
                return url.openStream();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }
