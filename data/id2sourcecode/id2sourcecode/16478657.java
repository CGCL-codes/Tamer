    public static void imageMark(File srcFile, File destFile, int minWidth, int minHeight, int pos, int offsetX, int offsetY, String markContent, Color markColor, int markSize, int alpha) throws IOException, MagickException {
        ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
        MagickImage image = new MagickImage(info);
        Dimension dim = image.getDimension();
        int width = (int) dim.getWidth();
        int height = (int) dim.getHeight();
        if (width < minWidth || height < minHeight) {
            image.destroyImages();
            if (!srcFile.equals(destFile)) {
                FileUtils.copyFile(srcFile, destFile);
            }
        } else {
            imageMark(image, info, width, height, pos, offsetX, offsetY, markContent, markColor, markSize, alpha);
            image.setFileName(destFile.getAbsolutePath());
            image.writeImage(info);
            image.destroyImages();
        }
    }
