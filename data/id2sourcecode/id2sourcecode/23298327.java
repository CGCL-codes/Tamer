    private static BufferedImage loadImage(URL url, boolean translucent) {
        try {
            return loadImage(url.openStream(), translucent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
