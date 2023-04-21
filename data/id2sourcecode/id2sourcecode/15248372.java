        protected void reload() {
            for (int strideX = 0; strideX < width; strideX += 4) {
                int bits = memoryReader.readNext();
                int color = memoryReader.readNext();
                readAlpha();
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
                boolean color3transparent;
                if (color0 > color1 || dxtLevel > 1) {
                    r3 = (r0 + r1 * 2) / 3;
                    g3 = (g0 + g1 * 2) / 3;
                    b3 = (b0 + b1 * 2) / 3;
                    color3transparent = false;
                } else {
                    r3 = 0x00;
                    g3 = 0x00;
                    b3 = 0x00;
                    color3transparent = true;
                }
                colors[0] = (b0 << 16) | (g0 << 8) | (r0);
                colors[1] = (b1 << 16) | (g1 << 8) | (r1);
                colors[2] = (b2 << 16) | (g2 << 8) | (r2);
                colors[3] = (b3 << 16) | (g3 << 8) | (r3);
                storePixels(strideX, bits, color3transparent);
            }
        }
