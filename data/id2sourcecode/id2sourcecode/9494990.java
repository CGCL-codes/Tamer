    private void copyScreen() {
        frame.setVisible(false);
        background = robot.createScreenCapture(bounds);
        frame.setVisible(true);
        System.out.println(background.getHeight(this));
        reflesh();
    }
