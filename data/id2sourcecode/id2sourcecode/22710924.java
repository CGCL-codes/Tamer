    public void run(Rectangle bounds) {
        this.bounds = bounds;
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(0, 0, size.width, size.height));
            JDialog frame = new RegionSelectorDialog(image);
            frame.setUndecorated(true);
            frame.setBounds(0, 0, size.width, size.height);
            frame.setModal(true);
            frame.setVisible(true);
        } catch (AWTException ae) {
            ae.printStackTrace();
        }
    }
