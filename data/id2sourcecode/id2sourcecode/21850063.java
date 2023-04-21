    private boolean doPixel(int x1, int y1, int x2, int y2, int[] pixels, int stride, int depth, int scale) {
        int mx, my;
        if (depth == 0) {
            int ml, mr, mt, mb, mm, t;
            int tl = getPixel(x1, y1, pixels, stride);
            int bl = getPixel(x1, y2, pixels, stride);
            int tr = getPixel(x2, y1, pixels, stride);
            int br = getPixel(x2, y2, pixels, stride);
            float amount = (256.0f / (2.0f * scale)) * turbulence;
            mx = (x1 + x2) / 2;
            my = (y1 + y2) / 2;
            if (mx == x1 && mx == x2 && my == y1 && my == y2) return true;
            if (mx != x1 || mx != x2) {
                ml = average(tl, bl);
                ml = displace(ml, amount);
                putPixel(x1, my, ml, pixels, stride);
                if (x1 != x2) {
                    mr = average(tr, br);
                    mr = displace(mr, amount);
                    putPixel(x2, my, mr, pixels, stride);
                }
            }
            if (my != y1 || my != y2) {
                if (x1 != mx || my != y2) {
                    mb = average(bl, br);
                    mb = displace(mb, amount);
                    putPixel(mx, y2, mb, pixels, stride);
                }
                if (y1 != y2) {
                    mt = average(tl, tr);
                    mt = displace(mt, amount);
                    putPixel(mx, y1, mt, pixels, stride);
                }
            }
            if (y1 != y2 || x1 != x2) {
                mm = average(tl, br);
                t = average(bl, tr);
                mm = average(mm, t);
                mm = displace(mm, amount);
                putPixel(mx, my, mm, pixels, stride);
            }
            if (x2 - x1 < 3 && y2 - y1 < 3) return false;
            return true;
        }
        mx = (x1 + x2) / 2;
        my = (y1 + y2) / 2;
        doPixel(x1, y1, mx, my, pixels, stride, depth - 1, scale + 1);
        doPixel(x1, my, mx, y2, pixels, stride, depth - 1, scale + 1);
        doPixel(mx, y1, x2, my, pixels, stride, depth - 1, scale + 1);
        return doPixel(mx, my, x2, y2, pixels, stride, depth - 1, scale + 1);
    }
