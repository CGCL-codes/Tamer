    public Image getImage(URL url) {
        try {
            return new Image(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }
