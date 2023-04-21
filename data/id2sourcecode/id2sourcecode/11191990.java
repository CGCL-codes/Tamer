    private void drawXpBorder(Graphics g, int x, int y, int w, int h) {
        g.drawLine(x, y + 6, x, y + h - 1);
        g.drawLine(x + 2, y + titleHeight, x + 2, y + h - 3);
        g.drawLine(x + w - 1, y + 6, x + w - 1, y + h - 1);
        g.drawLine(x + w - 3, y + titleHeight, x + w - 3, y + h - 3);
        g.drawLine(x + 2, y + h - 3, x + w - 3, y + h - 3);
        g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
        if (TinyLookAndFeel.ROBOT != null) {
            int wx = window.getLocationOnScreen().x - 4;
            int wy = window.getLocationOnScreen().y;
            theRect.setBounds(wx, wy, 4, 4);
            g.drawImage(TinyLookAndFeel.ROBOT.createScreenCapture(theRect), x, y, null);
            wx = window.getLocationOnScreen().x + window.getWidth() + 1;
            theRect.setBounds(wx, wy, 4, 4);
            g.drawImage(TinyLookAndFeel.ROBOT.createScreenCapture(theRect), x + w - 4, y, null);
        } else {
            g.setColor(Theme.backColor.getColor());
            g.fillRect(0, 0, w, 3);
        }
        if (isActive) {
            g.setColor(Theme.frameCaptionColor.getColor());
        } else {
            g.setColor(Theme.frameCaptionDisabledColor.getColor());
        }
        g.drawLine(x + 1, y + titleHeight, x + 1, y + h - 2);
        g.drawLine(x + w - 2, y + titleHeight, x + w - 2, y + h - 2);
        g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
        Color c = null;
        if (isActive) {
            c = Theme.frameBorderColor.getColor();
        } else {
            c = Theme.frameBorderDisabledColor.getColor();
        }
        g.setColor(ColorRoutines.getAlphaColor(c, 82));
        g.drawLine(x, y + 3, x, y + 3);
        g.drawLine(x + w - 1, y + 3, x + w - 1, y + 3);
        g.setColor(ColorRoutines.getAlphaColor(c, 156));
        g.drawLine(x, y + 4, x, y + 4);
        g.drawLine(x + w - 1, y + 4, x + w - 1, y + 4);
        g.setColor(ColorRoutines.getAlphaColor(c, 215));
        g.drawLine(x, y + 5, x, y + 5);
        g.drawLine(x + w - 1, y + 5, x + w - 1, y + 5);
        if (isActive) {
            c = Theme.frameCaptionColor.getColor();
        } else {
            c = Theme.frameCaptionDisabledColor.getColor();
        }
        int spread1 = Theme.frameSpreadDarkDisabled.getValue();
        int spread2 = Theme.frameSpreadLightDisabled.getValue();
        Color borderColor = null;
        if (isActive) {
            borderColor = Theme.frameBorderColor.getColor();
            spread1 = Theme.frameSpreadDark.getValue();
            spread2 = Theme.frameSpreadLight.getValue();
        }
        int y2 = 1;
        Color c2 = ColorRoutines.darken(c, 4 * spread1);
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 23));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        c2 = ColorRoutines.darken(c, 6 * spread1);
        g.setColor(c2);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.getAlphaColor(c2, 139));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        g.setColor(c);
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 6 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 2, y2, x + 2, y2);
        g.drawLine(x + w - 3, y2, x + w - 3, y2);
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.drawLine(x + 1, y2, x + 1, y2);
        g.drawLine(x + w - 2, y2, x + w - 2, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 4 * spread1));
        g.fillRect(x + 1, y2, 2, 2);
        g.fillRect(x + w - 3, y2, 2, 2);
        y2 += 2;
        g.setColor(ColorRoutines.darken(c, 3 * spread1));
        g.fillRect(x + 1, y2, 2, 4);
        g.fillRect(x + w - 3, y2, 2, 4);
        y2 += 4;
        g.setColor(ColorRoutines.darken(c, 2 * spread1));
        g.fillRect(x + 1, y2, 2, 3);
        g.fillRect(x + w - 3, y2, 2, 3);
        y2 += 3;
        g.setColor(ColorRoutines.darken(c, 1 * spread1));
        g.fillRect(x + 1, y2, 2, 2);
        g.fillRect(x + w - 3, y2, 2, 2);
        y2 += 2;
        g.setColor(c);
        g.fillRect(x + 1, y2, 2, 2);
        g.fillRect(x + w - 3, y2, 2, 2);
        y2 += 2;
        g.setColor(ColorRoutines.lighten(c, 2 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 4 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 5 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 6 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 8 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 9 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 10 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.lighten(c, 4 * spread2));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        g.setColor(ColorRoutines.darken(c, 2 * spread1));
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
        y2++;
        if (isActive) {
            g.setColor(Theme.frameLightColor.getColor());
        } else {
            g.setColor(Theme.frameLightDisabledColor.getColor());
        }
        g.drawLine(x + 1, y2, x + 2, y2);
        g.drawLine(x + w - 2, y2, x + w - 3, y2);
    }
