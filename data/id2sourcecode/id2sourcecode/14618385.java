    public URL convertToFileURL(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        if (connection instanceof BundleURLConnection) {
            return ((BundleURLConnection) connection).getFileURL();
        } else {
            return url;
        }
    }
