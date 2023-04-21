        public BlockArrowIcon(int width, int height, boolean direction, Color fg, Color bg) {
            this.width = width;
            this.height = height;
            this.fg = fg;
            this.bg = bg;
            int y0 = (direction ? 0 : height - 1);
            int d = (direction ? 1 : -1);
            int baseSize = (width + 1) / 2;
            xpoints = new int[8];
            ypoints = new int[8];
            xpoints[0] = baseSize / 2;
            ypoints[0] = y0;
            xpoints[1] = baseSize / 2;
            ypoints[1] = y0 + baseSize * d;
            xpoints[2] = 0;
            ypoints[2] = y0 + baseSize * d;
            xpoints[3] = baseSize - 1;
            ypoints[3] = y0 + (height - 1) * d;
            for (int i = 0; i < 4; i++) {
                xpoints[7 - i] = width - xpoints[i] - 1;
                ypoints[7 - i] = ypoints[i];
            }
        }
