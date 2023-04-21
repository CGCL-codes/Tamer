    private void createShadowPicture(BufferedImage image) {
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        int windowWidth = imageWidth;
        int windowHeight = imageHeight;
        if (addShadow) {
            windowWidth += nShadowPixels;
            windowHeight += nShadowPixels;
        }
        setSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();
        splash = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) splash.getGraphics();
        if (addShadow || honorTransparency) {
            try {
                Robot robot = new Robot(getGraphicsConfiguration().getDevice());
                BufferedImage capture = robot.createScreenCapture(new Rectangle(windowRect.x, windowRect.y, windowRect.width + nShadowPixels, windowRect.height + nShadowPixels));
                g2.drawImage(capture, null, 0, 0);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            if (addShadow) {
                BufferedImage shadow = new BufferedImage(imageWidth + nShadowPixels, imageHeight + nShadowPixels, BufferedImage.TYPE_INT_ARGB);
                Graphics g = shadow.getGraphics();
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
                g.fillRoundRect(6, 6, imageWidth, imageHeight, 12, 12);
                g2.drawImage(shadow, getBlurOp(7), 0, 0);
            }
        }
        g2.drawImage(image, 0, 0, this);
    }
