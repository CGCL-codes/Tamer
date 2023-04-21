    public void loadLeftImage() {
        try {
            Robot robot = new Robot();
            snapshot = robot.createScreenCapture(mainWindow.getGEPanelSize());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        repaint();
    }
