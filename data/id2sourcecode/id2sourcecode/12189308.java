    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        JRfbServer server = new JRfbServer(10, 400, 400, "Robot");
        server.start();
        Graphics g = server.getGraphics();
        Robot robot = new Robot();
        while (true) {
            BufferedImage img = robot.createScreenCapture(new Rectangle(400, 400));
            g.drawImage(img, 0, 0, null);
            PointerInfo a = MouseInfo.getPointerInfo();
            Point b = a.getLocation();
            int x = (int) b.getX();
            int y = (int) b.getY();
            g.fillRect(x, y, 10, 10);
            server.imageUpdated();
            Thread.sleep(1000);
        }
    }
