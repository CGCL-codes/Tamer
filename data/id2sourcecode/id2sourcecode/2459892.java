    public void updateBackground() {
        try {
            Dimension frameDim = frame.getSize();
            frame.setSize(0, 0);
            Robot rbt = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
            frame.setSize(frameDim);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
