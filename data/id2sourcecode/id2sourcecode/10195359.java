    protected void updateBackground() {
        this.setVisible(false);
        _background = _robot.createScreenCapture(_screenRect);
        this.setVisible(true);
    }
