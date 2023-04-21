    private BufferedImage getIconImage(int maxWaitingDelay) {
        Color backgroundColor = getBackground();
        BufferedImage imageWithWhiteBackgound = null;
        BufferedImage imageWithBlackBackgound = null;
        this.iconImageLock = new Object();
        try {
            Point canvas3DOrigin = new Point();
            SwingUtilities.convertPointToScreen(canvas3DOrigin, this.canvas3D);
            Robot robot = new Robot();
            if (this.iconImageLock != null) {
                synchronized (this.iconImageLock) {
                    setBackground(Color.WHITE);
                    try {
                        this.iconImageLock.wait(maxWaitingDelay / 2);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            imageWithWhiteBackgound = robot.createScreenCapture(new Rectangle(canvas3DOrigin, this.canvas3D.getSize()));
            if (this.iconImageLock != null) {
                synchronized (this.iconImageLock) {
                    setBackground(Color.BLACK);
                    try {
                        this.iconImageLock.wait(maxWaitingDelay / 2);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            imageWithBlackBackgound = robot.createScreenCapture(new Rectangle(canvas3DOrigin, this.canvas3D.getSize()));
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        } finally {
            this.iconImageLock = null;
            setBackground(backgroundColor);
        }
        int[] imageWithWhiteBackgoundPixels = imageWithWhiteBackgound.getRGB(0, 0, imageWithWhiteBackgound.getWidth(), imageWithWhiteBackgound.getHeight(), null, 0, imageWithWhiteBackgound.getWidth());
        int[] imageWithBlackBackgoundPixels = imageWithBlackBackgound.getRGB(0, 0, imageWithBlackBackgound.getWidth(), imageWithBlackBackgound.getHeight(), null, 0, imageWithBlackBackgound.getWidth());
        for (int i = 0; i < imageWithBlackBackgoundPixels.length; i++) {
            if (imageWithBlackBackgoundPixels[i] != imageWithWhiteBackgoundPixels[i] && imageWithBlackBackgoundPixels[i] == 0xFF000000 && imageWithWhiteBackgoundPixels[i] == 0xFFFFFFFF) {
                imageWithWhiteBackgoundPixels[i] = 0;
            }
        }
        BufferedImage iconImage = new BufferedImage(imageWithWhiteBackgound.getWidth(), imageWithWhiteBackgound.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = (Graphics2D) iconImage.getGraphics();
        g2D.drawImage(Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(imageWithWhiteBackgound.getWidth(), imageWithWhiteBackgound.getHeight(), imageWithWhiteBackgoundPixels, 0, imageWithWhiteBackgound.getWidth())), null, null);
        g2D.dispose();
        return iconImage;
    }
