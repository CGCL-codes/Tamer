    public LSTRFont(LFont font) {
        if (font.getSize() > 30) {
            throw new RuntimeException("Sorry ,font size > 30 are not supported !");
        }
        this.font = font;
        LImage tempImage = new LImage(defFontWidth, defFontHeight, Config.ARGB_8888);
        LGraphics g = tempImage.getLGraphics();
        g.setFont(LFont.getFont(font.getFontName(), font.getStyle(), font.getSize() + 5));
        g.setColor(LColor.white);
        g.setAntiAlias(true);
        int fontHeight = font.getHeight();
        int rows = 0, cols = font.getSize();
        for (int i = 0; i < totalCharSet; i++) {
            char chars = (char) i;
            String string = String.valueOf((char) i);
            int fontWidth = font.charWidth(chars);
            if (fontWidth < 1) {
                fontWidth = 1;
            }
            int posX = rows + (defSize - fontWidth) / 2;
            int posY = cols + (defSize - fontHeight) / 2;
            g.drawString(string, posX, posY);
            rows += defSize;
            if (rows >= defFontWidth) {
                rows = 0;
                cols += defSize;
            }
        }
        g.dispose();
        texture = new LTexture(GLLoader.getTextureData(tempImage), Format.FONT);
        texture.string = true;
        if (tempImage != null) {
            tempImage.dispose();
            tempImage = null;
        }
        LSystem.gc();
    }
