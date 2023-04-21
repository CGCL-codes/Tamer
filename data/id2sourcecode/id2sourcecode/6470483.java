    public void capture() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.sync();
        Rectangle ecran = new Rectangle(tk.getScreenSize());
        try {
            robot = new Robot();
        } catch (java.awt.AWTException awte) {
            awte.printStackTrace();
        }
        robot.setAutoDelay(0);
        robot.setAutoWaitForIdle(false);
        robot.delay(4000);
        setImage(robot.createScreenCapture(ecran));
    }
