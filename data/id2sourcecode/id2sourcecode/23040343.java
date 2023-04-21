    public void updateBackground() {
        try {
            Robot rbt = Jaguar.getRobby();
            Toolkit tk = frame.getToolkit();
            Dimension dim = tk.getScreenSize();
            background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
