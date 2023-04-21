    public BufferedImage getScreen(Rectangle rect) {
        BufferedImage img = null;
        try {
            Robot robot = new Robot();
            img = robot.createScreenCapture(rect);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            return img;
        }
    }
