    static ImageProducer Bitmap(byte[] imageData, int offset, int type) {
        if (WORD(imageData, offset + 12) != 1) throw new IllegalArgumentException("planes!=1");
        final int width = DWORD(imageData, offset + 4);
        final int h = DWORD(imageData, offset + 8);
        final int height = type != 0 ? h / 2 : h;
        final int bitCount = WORD(imageData, offset + 14);
        final int nColorsUsed = DWORD(imageData, offset + 32);
        final int colorCount = nColorsUsed != 0 ? nColorsUsed : 1 << bitCount;
        final int colorOffset = offset + DWORD(imageData, offset);
        final int compression = DWORD(imageData, offset + 16);
        if (compression != 0 && compression != 3) throw new IllegalArgumentException("compression!=0 && compression!=3");
        if (bitCount < 16 || compression == 3) {
            int idx = colorOffset;
            for (int i = 0; i < colorCount; ++i) {
                final byte b = imageData[idx];
                imageData[idx] = imageData[idx + 2];
                imageData[idx + 2] = b;
                imageData[idx + 3] = (byte) 255;
                idx += 4;
            }
        }
        final int[] intData = new int[height * width];
        int dst = 0;
        final int xorBytes = colorOffset + 4 * (bitCount < 16 ? colorCount : nColorsUsed);
        final int xorBytesPerRow = ((width * bitCount / 8) + 3) & ~3;
        final int andBytes = xorBytes + xorBytesPerRow * height;
        int xorOffset = xorBytes;
        int andOffset = andBytes;
        final int xorMask = bitCount < 32 ? (1 << bitCount) - 1 : 0xffffffff;
        final int pixelsPerByte = 8 / bitCount;
        final int bytesPerPixel = bitCount / 8;
        final int extraMaskBytesPerRow = (((width + 31) & ~31) - width) / 8;
        for (int y = 0; y < height; ++y) {
            int xPixel = 0, aPixel = 0;
            byte xData = 0, aData = 0;
            for (int x = 0; x < width; ++x) {
                int argb;
                if (bitCount < 16) {
                    if (--xPixel < 0) {
                        xData = imageData[xorOffset++];
                        xPixel = pixelsPerByte - 1;
                    }
                    argb = TRIPLE(imageData, colorOffset + ((xData >> (xPixel * bitCount)) & xorMask) * 4);
                } else {
                    argb = (imageData[xorOffset] & 0xff) | ((imageData[xorOffset + 1] & 0xff) << 8) | ((imageData[xorOffset + 2] & 0xff) << 16);
                    if (compression == 3) argb = TRIPLE(imageData, colorOffset + argb * 4); else argb |= 0xff000000;
                    xorOffset += bytesPerPixel;
                }
                if (type != 0) {
                    if (--aPixel < 0) {
                        aData = imageData[andOffset++];
                        aPixel = 7;
                    }
                    if ((aData & (1 << aPixel)) != 0) {
                        argb &= 0xffffff;
                    }
                }
                intData[dst++] = argb;
            }
            andOffset += extraMaskBytesPerRow;
        }
        return new MemoryImageSource(width, height, intData, dst - width, -width);
    }
