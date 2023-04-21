    private void captureBackground() {
        try {
            Robot robot = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            background = robot.createScreenCapture(new Rectangle(0, 0, (int) dim.getWidth(), (int) dim.getHeight()));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
