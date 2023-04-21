    public void updateBackground() {
        try {
            Robot rbt = new Robot();
            background = rbt.createScreenCapture(new Rectangle(frame.getX(), frame.getY(), 50, 50));
        } catch (Exception ex) {
            p(ex.toString());
            ex.printStackTrace();
        }
    }
