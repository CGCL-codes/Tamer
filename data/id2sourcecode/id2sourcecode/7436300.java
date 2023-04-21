    public void display(int width, int height) throws AWTException {
        frame.setVisible(false);
        BufferedImage image;
        image = getImage();
        if (image == null) {
            Frame[] fs = Frame.getFrames();
            for (int i = 0; i < fs.length; i++) {
                Frame tempFrame = fs[i];
                if (tempFrame != null) {
                    tempFrame.paint(tempFrame.getGraphics());
                }
            }
            Rectangle selectedArea = new Rectangle(0, 0, width, height);
            Robot robot = new Robot();
            image = robot.createScreenCapture(selectedArea);
            underlyingImage = image;
        }
        int newWidth = new Double(image.getWidth() * scale).intValue();
        int newHeight = new Double(image.getHeight() * scale).intValue();
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        showImage(scaledImage);
        frame.add("Center", this);
        Dimension d = new Dimension();
        d.setSize(scaledImage.getWidth(this), scaledImage.getHeight(this));
        frame.setSize(d);
        frame.setVisible(true);
        needRefresh = false;
    }
