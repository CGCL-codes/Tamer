    public void oamDMA(byte address) {
        int i, start;
        start = (address & 0xFF) * 0x0100;
        for (i = 0; i < 0xA0; i++) {
            video.write(0xFE00 + i, read(start + i));
        }
    }
