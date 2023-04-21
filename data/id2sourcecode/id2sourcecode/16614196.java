    public static Image getImage(URL url) {
        if (url == null) return null;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = null;
        try {
            url.openConnection().connect();
            image = toolkit.getImage(url);
            for (int i = 0; i < 4 && image.getWidth(null) < 0; i++) try {
                Thread.sleep(80);
            } catch (Exception e) {
            }
        } catch (java.io.IOException e) {
            System.err.println("ERROR: can't read the file " + url.toString());
        }
        return image;
    }
