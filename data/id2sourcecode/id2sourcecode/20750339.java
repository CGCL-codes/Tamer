    private void takeScreenshot(Point locationOnScreen) {
        Color pickedColor = robot.getPixelColor(locationOnScreen.x, locationOnScreen.y);
        A03ColorPicker.this.chooser.setColor(pickedColor);
        locationOnScreen.x -= 5;
        locationOnScreen.y -= 5;
        screenshotAreaImage = robot.createScreenCapture(new Rectangle(locationOnScreen, new Dimension(11, 11)));
    }
