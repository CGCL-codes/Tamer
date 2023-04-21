    public static Font loadFont(URL url) {
        Font font;
        try {
            InputStream is = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            is.close();
        } catch (Exception e) {
            font = new Font("arial", Font.PLAIN, 12);
            System.err.println("Unable to load font: " + url.getPath());
        }
        return font;
    }
