    protected ImageScale(File srcFile, File destFile, int zoomWidth, int zoomHeight) throws IOException {
        this.destFile = destFile;
        this.zoomWidth = zoomWidth;
        this.zoomHeight = zoomHeight;
        this.srcBufferImage = ImageIO.read(srcFile);
        this.width = this.srcBufferImage.getWidth();
        this.height = this.srcBufferImage.getHeight();
        if (width <= zoomWidth && height <= zoomHeight) {
            FileUtils.copyFile(srcFile, destFile);
        } else {
            resizeFix();
        }
    }
