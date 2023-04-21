    public Image createImage(URL url) {
        Image image;
        try {
            image = createImage(url.openStream());
        } catch (IOException ex) {
            image = createErrorImage();
        }
        return image;
    }
