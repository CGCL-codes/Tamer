    public static BufferedImage capture(int windowID) throws IOException {
        NativeWindow window = NativeWindow.getWindow(windowID);
        if (window == null) {
            throw new IOException("invalid window handle");
        }
        if (!window.isVisible()) {
            throw new IOException("window not visible");
        }
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new IOException(e);
        }
        BufferedImage capture = null;
        if (window.isVisible()) {
            Rectangle bounds = window.getBounds();
            Rectangle dimensions = new Rectangle(bounds.x, bounds.y, bounds.width - bounds.x, bounds.height - bounds.y);
            capture = robot.createScreenCapture(dimensions);
            BufferedImage pointer = ImageIO.read(new File("res/icons/cursor.png"));
            putPointer(dimensions, capture, pointer);
        }
        return capture;
    }
