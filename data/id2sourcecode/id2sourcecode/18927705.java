    public ImageInfo(BufferedImage buf, int fileSize) {
        this.fileSize = fileSize;
        bufImg = buf;
        w = buf.getWidth();
        h = buf.getHeight();
        int type = buf.getType();
        if (type == BufferedImage.TYPE_BYTE_GRAY) {
            actualColorSpace = new GrayScale();
        } else if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_3BYTE_BGR || type == BufferedImage.TYPE_INT_BGR) {
            actualColorSpace = new RGB();
        } else {
            actualColorSpace = new RGB();
        }
        bitsToWrite = actualColorSpace.getBitsPerChannel() * actualColorSpace.getChannelsNumber();
        originalColorSpace = actualColorSpace;
    }
