    private void centreWindow(javax.swing.JFrame parentFrame) {
        Point parentLocation = parentFrame.getLocation();
        Dimension parentSize = parentFrame.getSize();
        int parentPosX = (int) parentLocation.getX();
        int parentPosY = (int) parentLocation.getY();
        int parentSizeX = (int) parentSize.getWidth();
        int parentSizeY = (int) parentSize.getHeight();
        int sizeX = (int) getSize().getWidth();
        int sizeY = (int) getSize().getHeight();
        int posX = parentPosX + (parentSizeX - sizeX) / 2;
        int posY = parentPosY + (parentSizeY - sizeY) / 2;
        setLocation(posX, posY);
    }
