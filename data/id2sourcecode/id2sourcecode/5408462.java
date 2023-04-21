    private void paintButtonBody(Graphics2D g2, AbstractButton b, int x, int y, int w, int h, boolean isPressed, boolean isRollover, boolean isSelected) {
        Shape vOldClip = g2.getClip();
        if (position == POSITION_LEFT) {
            Shape vButtonShape = new RoundRectangle2D.Double((double) x, (double) y, (double) w + h, (double) h - 2, h - 2, h - 2);
            g2.setClip(vButtonShape);
        } else if (position == POSITION_RIGHT) {
            Shape vButtonShape = new RoundRectangle2D.Double((double) x - h - 1, (double) y, (double) w + h, (double) h - 2, h - 2, h - 2);
            g2.setClip(vButtonShape);
        } else if (position == POSITION_CENTER) {
            Shape vButtonShape = new Rectangle2D.Double((double) x, (double) y, (double) w, (double) h - 2);
            g2.setClip(vButtonShape);
        }
        Color vTopStartColor = topStartColor;
        Color vTopEndColor = topEndColor;
        Color vBottomStartColor = bottomStartColor;
        Color vBottomEndColor = bottomEndColor;
        if (isPressed || isSelected) {
            vTopStartColor = pressedTopStartColor;
            vTopEndColor = pressedTopEndColor;
            vBottomStartColor = pressedBottomStartColor;
            vBottomEndColor = pressedBottomEndColor;
        } else if (isRollover) {
            vTopStartColor = rolloverTopStartColor;
            vTopEndColor = rolloverTopEndColor;
            vBottomStartColor = rolloverBottomStartColor;
            vBottomEndColor = rolloverBottomEndColor;
        }
        Paint vTopPaint = new GradientPaint(0, y + 1, vTopStartColor, 0, y + h / 2 - 1, vTopEndColor);
        g2.setPaint(vTopPaint);
        g2.fillRect(x, y + 1, w, h / 2 - 1);
        Paint vBottomPaint = new GradientPaint(0, y + h / 2, vBottomStartColor, 0, y + h - 1, vBottomEndColor);
        g2.setPaint(vBottomPaint);
        g2.fillRect(x, y + h / 2, w, h / 2 - 1);
        g2.setClip(vOldClip);
        paintIcon(g2, b, isRollover, isSelected);
    }
