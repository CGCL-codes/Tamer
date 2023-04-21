    public void setLocationRelativeTo(Component c) {
        toolkit.lockAWT();
        try {
            Rectangle screenRect = getGraphicsConfiguration().getBounds();
            int minX = screenRect.x;
            int minY = screenRect.y;
            int maxX = minX + screenRect.width - 1;
            int maxY = minY + screenRect.height;
            int centerX = (minX + maxX) / 2;
            int centerY = (minY + maxY) / 2;
            int x = centerX;
            int y = centerY;
            Point loc = new Point(centerX, centerY);
            int compX = loc.x;
            Dimension compSize = new Dimension();
            if ((c != null) && c.isShowing()) {
                loc = c.getLocationOnScreen();
                compX = loc.x;
                compSize = c.getSize();
            }
            loc.translate(compSize.width / 2, compSize.height / 2);
            int w = getWidth(), h = getHeight();
            loc.translate(-w / 2, -h / 2);
            x = Math.max(loc.x, minX);
            y = Math.max(loc.y, minY);
            int right = x + w, bottom = y + h;
            if (right > maxX) {
                x -= right - maxX;
            }
            if (bottom > maxY) {
                y -= bottom - maxY;
                int compRight = compX + compSize.width;
                int distRight = Math.abs(compRight - centerX);
                int distLeft = Math.abs(centerX - compX);
                x = ((distRight < distLeft) ? compRight : (compX - w));
                x = Math.max(x, minX);
                right = x + w;
                if (right > maxX) {
                    x -= right - maxX;
                }
            }
            setLocation(x, y);
        } finally {
            toolkit.unlockAWT();
        }
    }
