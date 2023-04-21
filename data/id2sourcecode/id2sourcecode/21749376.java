    private void createShadowBorder() {
        backgroundImage = new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) backgroundImage.getGraphics();
        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture = robot.createScreenCapture(new Rectangle(getX(), getY(), getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH));
            g2.drawImage(capture, null, 0, 0);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        BufferedImage shadow = new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = shadow.getGraphics();
        graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
        graphics.fillRoundRect(6, 6, getWidth(), getHeight(), 12, 12);
        g2.drawImage(shadow, getBlurOp(7), 0, 0);
    }
