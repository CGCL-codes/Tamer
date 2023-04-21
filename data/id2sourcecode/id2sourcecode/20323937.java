    private void makeSnapshot() throws AWTException {
        Window oWindow = SwingUtilities.getWindowAncestor(this);
        Insets oInsets = oWindow.getInsets();
        Rectangle oRectangle = new Rectangle(oWindow.getBounds());
        oRectangle.x += oInsets.left;
        oRectangle.y += oInsets.top;
        oRectangle.width -= oInsets.left + oInsets.right;
        oRectangle.height -= oInsets.top + oInsets.bottom;
        imageBuf = new Robot().createScreenCapture(oRectangle);
    }
