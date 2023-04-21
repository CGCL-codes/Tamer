    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color origColor;
        origColor = g.getColor();
        int w = getWidth();
        int h = getHeight();
        Insets borderInsets = originalBorder.getBorderInsets(this);
        boolean isEnabled = isEnabled();
        int x;
        int y1 = borderInsets.top;
        int y2 = h - borderInsets.bottom;
        if (getComponentOrientation().isLeftToRight()) {
            x = w - arrowSpaceWidth - borderInsets.right + 1;
            int size = (arrowWidth + 1) / 2;
            paintTriangle(g, x + arrowSpaceWidth / 2, (h - size) - 5, size + 1, isEnabled);
        } else {
            x = arrowSpaceWidth + borderInsets.left - 1;
            int size = (arrowWidth + 1) / 2;
            paintTriangle(g, x - 1 - arrowWidth, (h - size) - 5, size + 1, isEnabled);
        }
        if (isDivided && (isMouseOver || hasFocus()) && isEnabled) {
            int gradientHeight = Math.max((y2 - y1 + 1) / 5, 1);
            float gradientIncrement = 100f / gradientHeight;
            Color foregroundColor = getForeground();
            for (int i = 0; i < gradientHeight; i++) {
                g.setColor(new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), (int) (gradientIncrement * (i + 1))));
                g.drawLine(x, y1 + i, x, y1 + i);
                g.drawLine(x, y2 - i, x, y2 - i);
            }
            Color dividerColor = new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), 100);
            g.setColor(dividerColor);
            g.drawLine(x, y1 + gradientHeight, x, y2 - gradientHeight);
        }
        g.setColor(origColor);
    }
