    private void doFixedPositionCapture() {
        Dimension screen;
        Rectangle rect;
        try {
            this.setVisible(false);
            Thread.sleep(50);
            Toolkit tk = Toolkit.getDefaultToolkit();
            screen = tk.getScreenSize();
            rect = new Rectangle(0, 0, screen.width, screen.height);
            if (lastPostionRectangle != null) {
                captureImage = robot.createScreenCapture(lastPostionRectangle);
                updates();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            screen = null;
            rect = null;
            screenImage = null;
        }
    }
