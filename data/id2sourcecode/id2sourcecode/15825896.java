        public int filterRGB(int _x, int _y, int _rgb) {
            int a = (_rgb & 0xff000000) >> 24;
            int r = (_rgb & 0x00ff0000) >> 16;
            int g = (_rgb & 0x0000ff00) >> 8;
            int b = (_rgb & 0x000000ff);
            int h = (299 * r + 587 * g + 114 * b) / 1000;
            a = (a + 256) % 256;
            h = (224 + h) / 2;
            a3_ = a2_;
            a2_ = a1_;
            a1_ = a;
            if (_x == 0) a2_ = a3_ = 0;
            if (_x == 1) a3_ = 0;
            a = 255;
            if ((a3_ != 255) && (a2_ != 255) && (a1_ == 255)) h = 128; else if ((a3_ != 255) && (a2_ == 255) && (a1_ == 255)) h = 255; else if ((a3_ == 255) && (a2_ == 255) && (a1_ != 255)) h = 128; else if ((a3_ == 255) && (a2_ != 255) && (a1_ != 255)) h = 255; else a = 0;
            r = h;
            g = h;
            b = h;
            if (r > 255) r = 255;
            if (r < 0) r = 0;
            if (g > 255) g = 255;
            if (g < 0) g = 0;
            if (b > 255) b = 255;
            if (b < 0) b = 0;
            return (a << 24) | (r << 16) | (g << 8) | b;
        }
