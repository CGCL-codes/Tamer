    public InputStream getResourceAsStream(String name) {
        InputStream is = null;
        try {
            URL url = getResource(name);
            if (url != null) {
                is = url.openStream();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return is;
    }
