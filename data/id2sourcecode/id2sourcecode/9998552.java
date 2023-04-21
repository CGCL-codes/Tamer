    public static int mixColors(int _rgb1, int _rgb2) {
        int a1 = (_rgb1 & 0xff000000) >> 24;
        int r1 = (_rgb1 & 0x00ff0000) >> 16;
        int g1 = (_rgb1 & 0x0000ff00) >> 8;
        int b1 = (_rgb1 & 0x000000ff);
        int a2 = (_rgb2 & 0xff000000) >> 24;
        int r2 = (_rgb2 & 0x00ff0000) >> 16;
        int g2 = (_rgb2 & 0x0000ff00) >> 8;
        int b2 = (_rgb2 & 0x000000ff);
        int a = (a1 + a2) / 2;
        int r = (r1 + r2) / 2;
        int g = (g1 + g2) / 2;
        int b = (b1 + b2) / 2;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
