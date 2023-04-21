    public void capture() {
        BufferedImage oldImage = this.image;
        try {
            Rectangle rect = new Rectangle(originX, originY, getWidth(), getHeight());
            this.image = new Robot().createScreenCapture(rect);
            propertyChangeSupport.firePropertyChange("image", oldImage, this.image);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
