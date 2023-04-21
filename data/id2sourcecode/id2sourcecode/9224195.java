    public static URL resolveExternalURL(String location) {
        if (location == null || location.trim().equals("")) {
            return null;
        }
        URL url = null;
        try {
            url = new URL(getCatalogResolver().getResolvedEntity(null, location));
            java.io.InputStream stream = url.openStream();
            stream.close();
        } catch (Exception e) {
        }
        if (url == null) {
            try {
                url = new URL(location);
                java.io.InputStream stream = url.openStream();
                stream.close();
            } catch (Exception e) {
                url = null;
            }
        }
        return url;
    }
