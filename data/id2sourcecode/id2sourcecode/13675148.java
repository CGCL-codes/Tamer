    private void decompressImageDXT(int dxtLevel) {
        IntBuffer decompressedBuffer = IntBuffer.allocate(round4(width) * round4(height));
        int strideX = 0;
        int strideY = 0;
        int[] colors = new int[4];
        int strideSize = (dxtLevel == 1 ? 8 : 16);
        int[] alphas = new int[16];
        int[] alphasLookup = new int[8];
        for (int i = 0; i < compressedImageSize; i += strideSize) {
            if (dxtLevel > 1) {
                if (dxtLevel <= 3) {
                    int alphaBits = 0;
                    for (int j = 0; j < 16; j++, alphaBits >>>= 4) {
                        if ((j % 8) == 0) {
                            alphaBits = getInt32(buffer);
                        }
                        int alpha = alphaBits & 0x0F;
                        alphas[j] = alpha << 4;
                    }
                } else {
                    int bits0 = getInt32(buffer);
                    int bits1 = getInt32(buffer);
                    int alpha0 = bits0 & 0xFF;
                    int alpha1 = (bits0 >> 8) & 0xFF;
                    alphasLookup[0] = alpha0;
                    alphasLookup[1] = alpha1;
                    if (alpha0 > alpha1) {
                        alphasLookup[2] = (6 * alpha0 + 1 * alpha1) / 7;
                        alphasLookup[3] = (5 * alpha0 + 2 * alpha1) / 7;
                        alphasLookup[4] = (4 * alpha0 + 3 * alpha1) / 7;
                        alphasLookup[5] = (3 * alpha0 + 4 * alpha1) / 7;
                        alphasLookup[6] = (2 * alpha0 + 5 * alpha1) / 7;
                        alphasLookup[7] = (1 * alpha0 + 6 * alpha1) / 7;
                    } else {
                        alphasLookup[2] = (4 * alpha0 + 1 * alpha1) / 5;
                        alphasLookup[3] = (3 * alpha0 + 2 * alpha1) / 5;
                        alphasLookup[4] = (2 * alpha0 + 3 * alpha1) / 5;
                        alphasLookup[5] = (1 * alpha0 + 4 * alpha1) / 5;
                        alphasLookup[6] = 0x00;
                        alphasLookup[7] = 0xFF;
                    }
                    int bits = bits0 >> 16;
                    for (int j = 0; j < 16; j++) {
                        int lookup;
                        if (j == 5) {
                            lookup = (bits & 1) << 2 | (bits1 & 3);
                            bits = bits1 >>> 2;
                        } else {
                            lookup = bits & 7;
                            bits >>>= 3;
                        }
                        alphas[j] = alphasLookup[lookup];
                    }
                }
            }
            int color = getInt32(buffer);
            int color0 = (color >> 0) & 0xFFFF;
            int color1 = (color >> 16) & 0xFFFF;
            int r0 = (color0 >> 8) & 0xF8;
            int g0 = (color0 >> 3) & 0xFC;
            int b0 = (color0 << 3) & 0xF8;
            int r1 = (color1 >> 8) & 0xF8;
            int g1 = (color1 >> 3) & 0xFC;
            int b1 = (color1 << 3) & 0xF8;
            int r2, g2, b2;
            if (color0 > color1) {
                r2 = (r0 * 2 + r1) / 3;
                g2 = (g0 * 2 + g1) / 3;
                b2 = (b0 * 2 + b1) / 3;
            } else {
                r2 = (r0 + r1) / 2;
                g2 = (g0 + g1) / 2;
                b2 = (b0 + b1) / 2;
            }
            int r3, g3, b3;
            if (color0 > color1 || dxtLevel > 1) {
                r3 = (r0 + r1 * 2) / 3;
                g3 = (g0 + g1 * 2) / 3;
                b3 = (b0 + b1 * 2) / 3;
            } else {
                r3 = 0x00;
                g3 = 0x00;
                b3 = 0x00;
            }
            colors[0] = ((b0 & 0xFF) << 16) | ((g0 & 0xFF) << 8) | (r0 & 0xFF);
            colors[1] = ((b1 & 0xFF) << 16) | ((g1 & 0xFF) << 8) | (r1 & 0xFF);
            colors[2] = ((b2 & 0xFF) << 16) | ((g2 & 0xFF) << 8) | (r2 & 0xFF);
            colors[3] = ((b3 & 0xFF) << 16) | ((g3 & 0xFF) << 8) | (r3 & 0xFF);
            int bits = getInt32(buffer);
            for (int y = 0, alphaIndex = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++, bits >>>= 2, alphaIndex++) {
                    int bgr = colors[bits & 3];
                    int alpha = alphas[alphaIndex] << 24;
                    storePixel(decompressedBuffer, strideX + x, strideY + y, bgr | alpha);
                }
            }
            strideX += 4;
            if (strideX >= width) {
                strideX = 0;
                strideY += 4;
            }
        }
        buffer.rewind();
        compressedImage = false;
        buffer = decompressedBuffer;
        bufferWidth = width;
        bufferStorage = GeCommands.TPSM_PIXEL_STORAGE_MODE_32BIT_ABGR8888;
    }
