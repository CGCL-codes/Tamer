    public void capture() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            Robot aRobot = new Robot();
            Rectangle rect = new Rectangle(0, 0, dim.width, dim.height);
            backgroundImage = aRobot.createScreenCapture(rect);
        } catch (AWTException awte) {
            System.out.println("robot excepton occurred");
        }
    }
