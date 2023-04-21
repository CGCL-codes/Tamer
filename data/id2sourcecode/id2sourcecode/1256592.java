    public static final void writeImage(File draw, File image, int x, int y) {
        try {
            writeImage(ImageIO.read(draw), ImageIO.read(image), FileUtil.getOutputStream(image), x, y);
        } catch (Exception e) {
            SysLog.error(e);
        }
    }
