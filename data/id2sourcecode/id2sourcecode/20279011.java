    @Override
    public void paint(Property property, GC gc, int x, int y, int width, int height) throws Exception {
        String text = getText(property);
        if (text != null) {
            {
                RGB rgb = ColorDialog.getRGB(text);
                if (rgb != null) {
                    Color swtColor = new Color(null, rgb);
                    Color oldBackground = gc.getBackground();
                    Color oldForeground = gc.getForeground();
                    try {
                        int width_c = SAMPLE_SIZE;
                        int height_c = SAMPLE_SIZE;
                        int x_c = x;
                        int y_c = y + (height - height_c) / 2;
                        {
                            int delta = SAMPLE_SIZE + SAMPLE_MARGIN;
                            x += delta;
                            width -= delta;
                        }
                        {
                            gc.setBackground(swtColor);
                            gc.fillRectangle(x_c, y_c, width_c, height_c);
                        }
                        gc.setForeground(IColorConstants.gray);
                        gc.drawRectangle(x_c, y_c, width_c, height_c);
                    } finally {
                        gc.setBackground(oldBackground);
                        gc.setForeground(oldForeground);
                        swtColor.dispose();
                    }
                }
            }
            DrawUtils.drawStringCV(gc, text, x, y, width, height);
        }
    }
