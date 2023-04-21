    private void refreshImage() {
        BufferedImage posImg = robot.createScreenCapture(new Rectangle(offsetX - 50, offsetY - 50, 100, 100));
        for (int i = 0; i < posImg.getWidth(); i++) {
            posImg.setRGB(i, 50, 16711680);
        }
        for (int i = 0; i < posImg.getHeight(); i++) {
            posImg.setRGB(50, i, 16711680);
        }
        ImageIcon icon = new ImageIcon(posImg);
        lblPicture.setIcon(icon);
        posImg = null;
    }
