    public void drawFont(int fontSlot, int size, Point point, boolean isBlack, G15Position position, String[] text) {
        synchronized (image) {
            final String slotName = "Font-" + fontSlot + ((emulateComposer) ? "-" + size : "");
            if (!fontSlots.containsKey(slotName)) {
                return;
            }
            final Font font = fontSlots.get(slotName);
            graphicsArea.setFont(font);
            final Rectangle2D[] bounds = new Rectangle2D[text.length];
            final LineMetrics[] metrics = new LineMetrics[text.length];
            for (int i = 0; i < text.length; ++i) {
                bounds[i] = font.getStringBounds(text[i], graphicsArea.getFontRenderContext());
                metrics[i] = font.getLineMetrics(text[i], graphicsArea.getFontRenderContext());
            }
            int currentTop = point.y;
            for (int i = 0; i < text.length; ++i) {
                currentTop += bounds[i].getHeight();
                int y = Math.round(currentTop - metrics[i].getDescent());
                int x = point.x;
                int fontWidth = (int) bounds[i].getWidth();
                if (position == G15Position.CENTER) {
                    if (emulateComposer) {
                        x = 0;
                    }
                    int midpoint = x + (LCD_WIDTH - x) / 2;
                    x = midpoint - (fontWidth / 2);
                } else if (position == G15Position.RIGHT) {
                    x = LCD_WIDTH - fontWidth;
                }
                graphicsArea.drawString(text[i], x, y);
            }
        }
    }
