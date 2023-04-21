    public static final void writeString(String text, File file, int x, int y) {
        try {
            writeString(text, ImageIO.read(file), FileUtil.getOutputStream(file), x, y);
        } catch (Exception e) {
            WdLogs.error(e);
        }
    }
