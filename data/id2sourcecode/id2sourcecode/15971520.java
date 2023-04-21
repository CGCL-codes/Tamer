    private static void hideDesktop() {
        try {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            Robot robot = new Robot();
            final BufferedImage screenImage = robot.createScreenCapture(new Rectangle(0, 0, size.width, size.height));
            final Color overlay = new Color(0.5f, 0.5f, 0.5f, 0.9f);
            Window w = new Window(new Frame()) {

                public void paint(Graphics g) {
                    g.drawImage(screenImage, 0, 0, null);
                    g.setColor(overlay);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            w.setSize(size);
            w.setVisible(true);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
