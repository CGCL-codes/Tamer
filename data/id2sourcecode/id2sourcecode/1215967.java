    public void updateBackground(int x, int y) {
        this.background = robot.createScreenCapture(new Rectangle(x, y, x + this.window.getWidth(), y + this.window.getHeight()));
    }
