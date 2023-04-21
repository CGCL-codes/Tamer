    public void drawText(FontSize size, Point point, G15Position position, String text) {
        synchronized (image) {
            G15Font font = size.getFont();
            if (font != null) {
                int startPos = point.x;
                int fontWidth = text.length() * font.getSize().width;
                if (position == G15Position.CENTER) {
                    if (emulateComposer) {
                        startPos = 0;
                    }
                    int midpoint = startPos + (LCD_WIDTH - startPos) / 2;
                    startPos = midpoint - (fontWidth / 2);
                } else if (position == G15Position.RIGHT) {
                    startPos = LCD_WIDTH - fontWidth;
                }
                for (int i = 0; i < text.length(); i++) {
                    for (int x = 0; x < font.getSize().width; x++) {
                        for (int y = 0; y < font.getSize().height; y++) {
                            int ypos = point.y + y;
                            int xpos = startPos + x + (font.getSize().width * i);
                            if (xpos >= 0 && xpos < LCD_WIDTH && ypos >= 0 && ypos < LCD_HEIGHT) {
                                image.setRGB(xpos, ypos, font.getPixelColor(text.charAt(i), x, y).getRGB());
                            }
                        }
                    }
                }
            }
        }
    }
