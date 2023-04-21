    public static void main(String[] args) throws AWTException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        java.awt.Robot r = new Robot(gd);
        Rectangle rect = new Rectangle(0, 0, 1024, 768);
        long time = System.currentTimeMillis();
        int sampleSize = 50;
        for (int x = 0; x < sampleSize; x++) {
            BufferedImage bi = r.createScreenCapture(rect);
        }
        time = System.currentTimeMillis() - time;
        long fps = ((1000) * (sampleSize)) / time;
        System.out.println("FPS: " + fps);
    }
