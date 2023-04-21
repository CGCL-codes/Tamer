    public static Image imageFromURL(URL url) throws IOException, NotImageException {
        url.openStream().close();
        Image image = TOOLKIT.getImage(url);
        ensureImage(image);
        return image;
    }
