    private synchronized void checkInitialized() {
        if (!initialized) {
            int awtStyle = 0;
            if (style.indexOf("plain") != -1) {
                awtStyle |= Font.PLAIN;
            }
            if (style.indexOf("bold") != -1) {
                awtStyle |= Font.BOLD;
            }
            if (style.indexOf("italic") != -1) {
                awtStyle |= Font.ITALIC;
            }
            if (style.indexOf("underlined") != -1) {
            }
            if (antialiasing) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            } else {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
            try {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
                fontMetrics = graphics.getFontMetrics(baseFont.deriveFont(awtStyle, size));
                initialized = true;
            } catch (FontFormatException ex) {
                Logger.error(ex);
            } catch (IOException ex) {
                Logger.error(ex);
            }
        }
    }
