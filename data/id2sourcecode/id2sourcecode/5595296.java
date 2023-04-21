    public Popup getPopup(JPopupMenu popupMenu, int x, int y) {
        if (!isMenuOpaque()) {
            try {
                Dimension size = popupMenu.getPreferredSize();
                Rectangle screenRect = new Rectangle(x, y, size.width, size.height);
                screenImage = getRobot().createScreenCapture(screenRect);
            } catch (Exception ex) {
                screenImage = null;
            }
        }
        return super.getPopup(popupMenu, x, y);
    }
