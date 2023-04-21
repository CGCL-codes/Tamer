    public static Font getFont(String name, int style, int size) {
        Font base = null;
        if (mFonts.containsKey(name)) {
            base = (Font) mFonts.get(name);
        } else {
            String path = getSetting(FONT_DIR) + name + ".ttf";
            URL url = path.getClass().getResource("/" + path);
            if (url == null) {
                File file = new File(path);
                try {
                    url = file.toURL();
                } catch (MalformedURLException mue) {
                }
            }
            if (url != null) {
                try {
                    base = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
                } catch (IOException ioex) {
                } catch (FontFormatException ffe) {
                }
            }
        }
        if (base != null) {
            mFonts.put(name, base);
            return base.deriveFont(style, size);
        }
        return null;
    }
