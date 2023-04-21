    private TypedStream tryOpen(String uri) {
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            return new TypedStream(is);
        } catch (Exception ex) {
            return null;
        }
    }
