    public Object execute(Robot robot) throws IOException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Rectangle shotArea = new Rectangle(defaultToolkit.getScreenSize());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);
        encoder.encode(robot.createScreenCapture(shotArea));
        return bout.toByteArray();
    }
