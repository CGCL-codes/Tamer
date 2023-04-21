    public static void resizeFix(File srcFile, File destFile, int boxWidth, int boxHeight) throws IOException {
        BufferedImage srcImgBuff = ImageIO.read(srcFile);
        int width = srcImgBuff.getWidth();
        int height = srcImgBuff.getHeight();
        if (width <= boxWidth && height <= boxHeight) {
            FileUtils.copyFile(srcFile, destFile);
            return;
        }
        int zoomWidth;
        int zoomHeight;
        if ((float) width / height > (float) boxWidth / boxHeight) {
            zoomWidth = boxWidth;
            zoomHeight = Math.round((float) boxWidth * height / width);
        } else {
            zoomWidth = Math.round((float) boxHeight * width / height);
            zoomHeight = boxHeight;
        }
        BufferedImage imgBuff = scaleImage(srcImgBuff, width, height, zoomWidth, zoomHeight);
        writeFile(imgBuff, destFile);
    }
