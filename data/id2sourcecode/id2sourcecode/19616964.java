    @Override
    public void paint(final Graphics g) {
        boolean leftToRight = super.getComponentOrientation().isLeftToRight();
        boolean isEnabled = getParent().isEnabled();
        Color arrowColor = isEnabled ? new Color(102, 255, 0) : MetalLookAndFeel.getControlDisabled();
        boolean isPressed = getModel().isPressed();
        int width = getWidth();
        int height = getHeight();
        int w = width;
        int h = height;
        int arrowHeight = (height + 1) / 4;
        int arrowWidth = (height + 1) / 2;
        if (isPressed) {
            g.setColor(MetalLookAndFeel.getControlShadow());
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, width, height);
        if (getDirection() == NORTH) {
            if (!myFreeStanding) {
                height += 1;
                g.translate(0, -1);
                width += 2;
                if (!leftToRight) {
                    g.translate(-1, 0);
                }
            }
            try {
                BufferedImage btn = BPMain.getImage("scroll_track_btn");
                g.drawImage(btn, 0, 0, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            g.setColor(arrowColor);
            int startY = ((h + 1) - arrowHeight) / 2;
            int startX = (w / 2);
            for (int line = 0; line < arrowHeight; line++) {
                g.drawLine(startX - line, startY + line, startX + line + 1, startY + line);
            }
            if (isEnabled) {
                g.setColor(wh40kHighlightColor);
                if (!isPressed) {
                    g.drawLine(1, 1, width - 3, 1);
                    g.drawLine(1, 1, 1, height - 1);
                }
                g.drawLine(width - 1, 1, width - 1, height - 1);
                g.setColor(wh40kShadowColor);
                g.drawLine(0, 0, width - 2, 0);
                g.drawLine(0, 0, 0, height - 1);
                g.drawLine(width - 2, 2, width - 2, height - 1);
            } else {
                drawDisabledBorder(g, 0, 0, width, height + 1);
            }
            if (!myFreeStanding) {
                height -= 1;
                g.translate(0, 1);
                width -= 2;
                if (!leftToRight) {
                    g.translate(1, 0);
                }
            }
        } else if (getDirection() == SOUTH) {
            if (!myFreeStanding) {
                height += 1;
                width += 2;
                if (!leftToRight) {
                    g.translate(-1, 0);
                }
            }
            g.setColor(arrowColor);
            int startY = (((h + 1) - arrowHeight) / 2) + arrowHeight - 1;
            int startX = (w / 2);
            for (int line = 0; line < arrowHeight; line++) {
                g.drawLine(startX - line, startY - line, startX + line + 1, startY - line);
            }
            if (isEnabled) {
                g.setColor(wh40kHighlightColor);
                if (!isPressed) {
                    g.drawLine(1, 0, width - 3, 0);
                    g.drawLine(1, 0, 1, height - 3);
                }
                g.drawLine(1, height - 1, width - 1, height - 1);
                g.drawLine(width - 1, 0, width - 1, height - 1);
                g.setColor(wh40kShadowColor);
                g.drawLine(0, 0, 0, height - 2);
                g.drawLine(width - 2, 0, width - 2, height - 2);
                g.drawLine(2, height - 2, width - 2, height - 2);
            } else {
                drawDisabledBorder(g, 0, -1, width, height + 1);
            }
            if (!myFreeStanding) {
                height -= 1;
                width -= 2;
                if (!leftToRight) {
                    g.translate(1, 0);
                }
            }
        } else if (getDirection() == EAST) {
            if (!myFreeStanding) {
                height += 2;
                width += 1;
            }
            g.setColor(arrowColor);
            int startX = (((w + 1) - arrowHeight) / 2) + arrowHeight - 1;
            int startY = (h / 2);
            for (int line = 0; line < arrowHeight; line++) {
                g.drawLine(startX - line, startY - line, startX - line, startY + line + 1);
            }
            if (isEnabled) {
                g.setColor(wh40kHighlightColor);
                if (!isPressed) {
                    g.drawLine(0, 1, width - 3, 1);
                    g.drawLine(0, 1, 0, height - 3);
                }
                g.drawLine(width - 1, 1, width - 1, height - 1);
                g.drawLine(0, height - 1, width - 1, height - 1);
                g.setColor(wh40kShadowColor);
                g.drawLine(0, 0, width - 2, 0);
                g.drawLine(width - 2, 2, width - 2, height - 2);
                g.drawLine(0, height - 2, width - 2, height - 2);
            } else {
                drawDisabledBorder(g, -1, 0, width + 1, height);
            }
            if (!myFreeStanding) {
                height -= 2;
                width -= 1;
            }
        } else if (getDirection() == WEST) {
            if (!myFreeStanding) {
                height += 2;
                width += 1;
                g.translate(-1, 0);
            }
            g.setColor(arrowColor);
            int startX = (((w + 1) - arrowHeight) / 2);
            int startY = (h / 2);
            for (int line = 0; line < arrowHeight; line++) {
                g.drawLine(startX + line, startY - line, startX + line, startY + line + 1);
            }
            if (isEnabled) {
                g.setColor(wh40kHighlightColor);
                if (!isPressed) {
                    g.drawLine(1, 1, width - 1, 1);
                    g.drawLine(1, 1, 1, height - 3);
                }
                g.drawLine(1, height - 1, width - 1, height - 1);
                g.setColor(wh40kShadowColor);
                g.drawLine(0, 0, width - 1, 0);
                g.drawLine(0, 0, 0, height - 2);
                g.drawLine(2, height - 2, width - 1, height - 2);
            } else {
                drawDisabledBorder(g, 0, 0, width + 1, height);
            }
            if (!myFreeStanding) {
                height -= 2;
                width -= 1;
                g.translate(1, 0);
            }
        }
    }
