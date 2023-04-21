    public static byte[] expandRLE4(byte[] rle, int off, int width, int height) {
        int lineBytes = (width * 4 + 31) / 32 * 4;
        int size = off + lineBytes * height;
        byte[] dib = new byte[size];
        Arrays.fill(dib, (byte) 0x00);
        System.arraycopy(rle, 0, dib, 0, off);
        int sidx = off;
        for (int y = 0; y < height; y++) {
            int didx = off + lineBytes * y;
            boolean high = true;
            int x = 0;
            while (x < width) {
                int code1 = rle[sidx++] & 0xff;
                int code2 = rle[sidx++] & 0xff;
                if (code1 == 0x00) {
                    boolean eol = false;
                    switch(code2) {
                        case 0x00:
                            if (x > 0) {
                                eol = true;
                            }
                            break;
                        case 0x01:
                            return dib;
                        case 0x02:
                            {
                                code1 = rle[sidx++] & 0xff;
                                code2 = rle[sidx++] & 0xff;
                                x += code1;
                                y += code2;
                                didx += code1 / 2 + lineBytes * code2;
                                if ((code1 % 2) != 0) {
                                    high = !high;
                                    if (high) {
                                        didx++;
                                    }
                                }
                                break;
                            }
                        default:
                            x += code2;
                            int cnt = (code2 + 1) / 2;
                            if (high) {
                                for (int i = 0; i < cnt; i++) {
                                    dib[didx++] = rle[sidx++];
                                }
                                if ((code2 % 2) != 0) {
                                    didx--;
                                    dib[didx] &= 0xf0;
                                    high = false;
                                }
                            } else {
                                for (int i = 0; i < cnt; i++) {
                                    dib[didx++] |= (rle[sidx] >>> 4) & 0x0f;
                                    dib[didx] |= (rle[sidx++] << 4) & 0xf0;
                                }
                                if ((code2 % 2) != 0) {
                                    dib[didx] = (byte) 0x00;
                                    high = true;
                                }
                            }
                            if ((cnt % 2) != 0) {
                                sidx++;
                            }
                            break;
                    }
                    if (eol) {
                        break;
                    }
                } else {
                    x += code1;
                    if (!high) {
                        dib[didx++] = (byte) ((code2 >>> 4) & 0x0f);
                        code2 = ((code2 >>> 4) & 0x0f) | ((code2 << 4) & 0xf0);
                        code1--;
                        high = true;
                    }
                    int cnt = (code1 + 1) / 2;
                    for (int i = 0; i < cnt; i++) {
                        dib[didx++] = (byte) code2;
                    }
                    if ((code1 % 2) != 0) {
                        didx--;
                        dib[didx] &= 0xf0;
                        high = false;
                    }
                }
            }
        }
        return dib;
    }
