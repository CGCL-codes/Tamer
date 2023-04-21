    void unpackLine(int[] lineBuf, int resIndex, int ch, int y, int flags) {
        int w = sizeX2 ? charMaxWidth / 2 : charMaxWidth;
        int h = sizeX2 ? charHeight / 2 : charHeight;
        int xOff = 0;
        int xOff1 = 0;
        if ((flags & ITALIC) != 0) {
            xOff = -h / 4 + y / 2 + charItalicOffset[ch];
            xOff1 = y & 1;
        }
        if (y >= 0 && y < h) {
            int bitPosBase = ((resIndex * h + y) * w) * 4;
            for (int x = 0; x < w * 2; x++) {
                int x2 = x + xOff;
                int a = 0;
                if (x2 >= 0 && x2 < w * 2) {
                    int bitPos = bitPosBase + x2 * 2;
                    a = ((charBits[bitPos / 8] >> (bitPos & 7)) & 3) * 85;
                }
                if (xOff1 != 0) {
                    int b = 0;
                    x2++;
                    if (x2 >= 0 && x2 < w * 2) {
                        int bitPos = bitPosBase + x2 * 2;
                        b = ((charBits[bitPos / 8] >> (bitPos & 7)) & 3) * 85;
                    }
                    a = (a + b) / 2;
                }
                lineBuf[x] = a;
            }
        } else {
            for (int x = 0; x < w * 2; x++) {
                lineBuf[x] = 0;
            }
        }
    }
