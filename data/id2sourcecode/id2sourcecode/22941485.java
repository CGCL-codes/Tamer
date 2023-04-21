        private void createShadowBorder() {
            fBackgroundImage = new BufferedImage(getWidth() + SHADOW_WIDTH, getHeight() + SHADOW_WIDTH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) fBackgroundImage.getGraphics();
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
            graphics.fillRect(SHADOW_WIDTH / 2, SHADOW_WIDTH / 2, getWidth(), getHeight());
            g2.drawImage(shadow, getBlurOp(SHADOW_WIDTH / 2), 0, 0);
            if (fImage != null) {
                g2.drawImage(fImage, 0, 0, null);
            }
        }
