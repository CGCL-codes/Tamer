    public static void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY, File markFile) throws IOException {
        BufferedImage imgBuff = ImageIO.read(srcFile);
        int width = imgBuff.getWidth();
        int height = imgBuff.getHeight();
        if (width <= minWidth || height <= minHeight) {
            imgBuff = null;
            if (!srcFile.equals(destFile)) {
                FileUtils.copyFile(srcFile, destFile);
            }
        } else {
            imageMark(imgBuff, width, height, pos, offsetX, offsetY, markFile);
            writeFile(imgBuff, destFile);
            imgBuff = null;
        }
    }
