    private final void update_box(int y, int x, int w, int[] buf) {
        int si = (y * W) + x;
        int p = 0;
        x <<= 3;
        y <<= 3;
        int h;
        int s;
        if (scale == 1) {
            s = w * 8;
            for (int n = 0; n < 8; n++) {
                for (int k = 0; k < w; k++) {
                    int m = screen[si++];
                    byte c0 = (byte) (m >>> 8 & 0xF);
                    byte c1 = (byte) (m >>> 12);
                    m &= 0xFF;
                    do buf[p++] = ((m & 1) == 0) ? c0 : c1; while ((m >>>= 1) != 0);
                }
                si += ((W / 8) - w);
            }
            h = 8;
        } else {
            h = scale << 3;
            s = w * h;
            for (int n = 0; n < 8; n++) {
                for (int k = 0; k < w; k++) {
                    int m = screen[si++];
                    byte c0 = (byte) (m >>> 8 & 0xF);
                    byte c1 = (byte) (m >>> 12);
                    m &= 0xFF;
                    do {
                        buf[p] = buf[p + 1] = buf[p + s] = buf[p + s + 1] = ((m & 1) == 0) ? c0 : c1;
                        p += 2;
                    } while ((m >>>= 1) != 0);
                }
                p += s;
                si += ((W / 8) - w);
            }
            x *= scale;
            y *= scale;
        }
        try {
            screenTransformer.doit(buf, x, y, h, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
