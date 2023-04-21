    private void captureScreen() {
        for (int x = 0; x < 1; x++) {
            try {
                Rectangle cap = new Rectangle();
                cap.x = this.getX();
                cap.y = this.getY();
                cap.width = this.getWidth();
                cap.height = this.getHeight();
                AWTUtilities.setWindowOpacity(this, 0.0f);
                screen = new Robot().createScreenCapture(cap);
                AWTUtilities.setWindowOpacity(this, 1.0f);
                drawScreen(1);
            } catch (AWTException ex) {
                Logger.getLogger(Magnifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        shot++;
    }
