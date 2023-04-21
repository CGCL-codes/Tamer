    public static BufferedImage getCurrentScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.printf("width=%f, height=%f%n", d.getWidth(), d.getHeight());
        BufferedImage img = null;
        try {
            img = new Robot().createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return img;
    }
