    public void paintIcon(Component c, Graphics g, int x, int y) {
        FontMetrics fm;
        int x0;
        int y0;
        Rectangle b;
        Icon cIcon;
        Graphics2D g2;
        g2 = (Graphics2D) g;
        x0 = x;
        if (tabbedPane == null) {
            tabbedPane = (JTabbedPane) c;
            tabbedPane.addMouseListener(getMouseListener());
            tabbedPane.addMouseMotionListener(getMouseMotionListener());
            tabbedPane.addChangeListener(getChangeListener());
        }
        if (icon != null) {
            icon.paintIcon(c, g, x0, y + ((height - icon.getIconHeight()) / 2));
            x0 += icon.getIconWidth();
            x0 += SPACE_WIDTH;
        }
        if (text != null) {
            if (JMeldSettings.getInstance().getEditor().isAntialiasEnabled()) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            fm = label.getFontMetrics(label.getFont());
            y0 = y + fm.getAscent() + ((height - fm.getHeight()) / 2);
            g.setFont(label.getFont());
            g.drawString(text, x0, y0);
            x0 += stringWidth;
            x0 += SPACE_WIDTH;
        }
        y0 = y + (height - CLOSE_ICON_HEIGHT) / 2;
        if (closeIcon != null) {
            cIcon = currentIcon;
            if (!isSelected()) {
                cIcon = closeIcon_disabled;
            }
            if (cIcon == null) {
                cIcon = closeIcon;
            }
            cIcon.paintIcon(c, g, x0, y0);
            closeBounds = new Rectangle(x0, y0, CLOSE_ICON_WIDTH, CLOSE_ICON_HEIGHT);
        } else {
            g.drawLine(x0, y0, x0 + CLOSE_ICON_HEIGHT, y0 + CLOSE_ICON_WIDTH);
            g.drawLine(x0 + CLOSE_ICON_HEIGHT, y0, x0, y0 + CLOSE_ICON_WIDTH);
            closeBounds = new Rectangle(x0, y0, CLOSE_ICON_WIDTH, CLOSE_ICON_HEIGHT);
        }
        x0 += CLOSE_ICON_WIDTH;
    }
