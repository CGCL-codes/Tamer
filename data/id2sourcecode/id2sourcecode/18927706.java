    public ImageInfo(int h, int w, LSColorSpace space, int[][] imgData, int fileSize) {
        this.h = h;
        this.w = w;
        this.fileSize = fileSize;
        bitsToWrite = space.getBitsPerChannel() * space.getChannelsNumber();
        originalColorSpace = actualColorSpace = space;
        createImage(imgData);
    }
