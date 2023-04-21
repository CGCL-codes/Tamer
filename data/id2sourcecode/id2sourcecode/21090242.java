    public void registerFont(String fontName, int fontFormat, java.io.InputStream fontStream) throws java.awt.FontFormatException, java.io.IOException {
        Font f = Font.createFont(fontFormat, fontStream);
        synchronized (this) {
            this.registeredFonts.put(fontName.toLowerCase(), f);
        }
    }
