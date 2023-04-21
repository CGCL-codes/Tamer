    @Override
    protected Font processUrl(URL url) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, url.openStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
