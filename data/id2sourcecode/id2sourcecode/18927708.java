    public ImageInfo(int h, int w, LSColorSpace space, File f) throws IOException {
        this.h = h;
        this.w = w;
        int[][] imageData = new int[h][w];
        originalColorSpace = space;
        bitsToWrite = space.getBitsPerChannel() * space.getChannelsNumber();
        actualColorSpace = (space.getCodifica() == LSColorSpace.GRY ? new GrayScale() : new RGB());
        ImageInputStream ios = ImageIO.createImageInputStream(f);
        space.convertToRGB(ios, w, h, imageData);
        createImage(imageData);
        fileSize = (int) ios.getStreamPosition();
        ios.close();
    }
