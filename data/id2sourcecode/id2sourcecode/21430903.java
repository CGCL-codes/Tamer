    public BufferedImage makeImage() {
        if (width == 0 || height == 0) {
            return null;
        }
        byte[] rChannel = getChannelData(Channel.RED);
        byte[] gChannel = getChannelData(Channel.GREEN);
        byte[] bChannel = getChannelData(Channel.BLUE);
        byte[] aChannel = getChannelData(Channel.ALPHA);
        applyOpacity(aChannel);
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] data = ((DataBufferInt) im.getRaster().getDataBuffer()).getData();
        int n = width * height - 1;
        while (n >= 0) {
            int a = aChannel[n] & 0xff;
            int r = rChannel[n] & 0xff;
            int g = gChannel[n] & 0xff;
            int b = bChannel[n] & 0xff;
            data[n] = a << 24 | r << 16 | g << 8 | b;
            n--;
        }
        return im;
    }
