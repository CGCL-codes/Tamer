    public BufferedImage getScreen(Rectangle rect) {
        BufferedImage img = null;
        try {
            markers = loadMarkers();
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
