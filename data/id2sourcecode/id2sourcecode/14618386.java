    public URL convertToLocalURL(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        if (connection instanceof BundleURLConnection) {
            return ((BundleURLConnection) connection).getLocalURL();
        } else {
            return url;
        }
    }
