    private Image takeScreenshot() {
        try {
            Robot robot = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            return robot.createScreenCapture(new Rectangle(0, 0, dim.width, dim.height));
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }
