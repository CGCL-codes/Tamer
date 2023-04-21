    public static ImageIcon getImageIcon(URL url) {
        if (url == null) return null;
        ImageIcon icon = null;
        try {
            url.openConnection().connect();
            icon = new ImageIcon(url);
        } catch (java.io.IOException e) {
            System.err.println("ERROR: can't read the file " + url.toString());
        }
        return icon;
    }
