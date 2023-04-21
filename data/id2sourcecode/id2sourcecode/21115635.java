    public void setVisible(boolean b) {
        if (!isVisible() && b) {
            try {
                image = new Robot().createScreenCapture(getBounds());
            } catch (AWTException e) {
                image = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
            }
        }
        super.setVisible(b);
    }
