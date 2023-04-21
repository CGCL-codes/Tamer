    private static ImageDescriptor createImageDescriptor(String path) {
        URL url = null;
        try {
            url = new URL(BASE_URL, path);
            url.openStream().close();
            return ImageDescriptor.createFromURL(url);
        } catch (Exception e) {
            Logger.trace("ImageStore", "Can't open URL: " + url);
        }
        return null;
    }
