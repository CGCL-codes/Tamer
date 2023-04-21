    public InputStream getIcon(int size) throws IOException {
        String url = getIconURL(size);
        if (url != null) {
            if (sourceURL != null) {
                return new URL(new URL(sourceURL, "/"), url).openStream();
            } else {
                return new URL(url).openStream();
            }
        }
        return null;
    }
