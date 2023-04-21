    void renderShapeUnclipped(int[] shades, boolean tScreened, int[] sphereShape, int diameter, int x, int y, int z) {
        int[] pbuf = g3d.pbuf;
        int[] zbuf = g3d.zbuf;
        int offsetSphere = 0;
        int width = g3d.width;
        int evenSizeCorrection = 1 - (diameter & 1);
        int offsetSouthCenter = width * y + x;
        int offsetNorthCenter = offsetSouthCenter - evenSizeCorrection * width;
        int nLines = (diameter + 1) / 2;
        if (!tScreened) {
            do {
                int offsetSE = offsetSouthCenter;
                int offsetSW = offsetSouthCenter - evenSizeCorrection;
                int offsetNE = offsetNorthCenter;
                int offsetNW = offsetNorthCenter - evenSizeCorrection;
                int packed;
                do {
                    packed = sphereShape[offsetSphere++];
                    int zPixel = z - (packed & 0x7F);
                    if (zPixel < zbuf[offsetSE]) {
                        zbuf[offsetSE] = zPixel;
                        pbuf[offsetSE] = shades[(packed >> 7) & 0x3F];
                    }
                    if (zPixel < zbuf[offsetSW]) {
                        zbuf[offsetSW] = zPixel;
                        pbuf[offsetSW] = shades[(packed >> 13) & 0x3F];
                    }
                    if (zPixel < zbuf[offsetNE]) {
                        zbuf[offsetNE] = zPixel;
                        pbuf[offsetNE] = shades[(packed >> 19) & 0x3F];
                    }
                    if (zPixel < zbuf[offsetNW]) {
                        zbuf[offsetNW] = zPixel;
                        pbuf[offsetNW] = shades[(packed >> 25) & 0x3F];
                    }
                    ++offsetSE;
                    --offsetSW;
                    ++offsetNE;
                    --offsetNW;
                } while (packed >= 0);
                offsetSouthCenter += width;
                offsetNorthCenter -= width;
            } while (--nLines > 0);
            return;
        }
        int flipflopSouthCenter = (x ^ y) & 1;
        int flipflopNorthCenter = flipflopSouthCenter ^ evenSizeCorrection;
        int flipflopSE = flipflopSouthCenter;
        int flipflopSW = flipflopSouthCenter ^ evenSizeCorrection;
        int flipflopNE = flipflopNorthCenter;
        int flipflopNW = flipflopNorthCenter ^ evenSizeCorrection;
        int flipflopsCenter = flipflopSE | (flipflopSW << 1) | (flipflopNE << 2) | (flipflopNW << 3);
        do {
            int offsetSE = offsetSouthCenter;
            int offsetSW = offsetSouthCenter - evenSizeCorrection;
            int offsetNE = offsetNorthCenter;
            int offsetNW = offsetNorthCenter - evenSizeCorrection;
            int packed;
            int flipflops = (flipflopsCenter = ~flipflopsCenter);
            do {
                packed = sphereShape[offsetSphere++];
                int zPixel = z - (packed & 0x7F);
                if ((flipflops & 1) != 0 && zPixel < zbuf[offsetSE]) {
                    zbuf[offsetSE] = zPixel;
                    pbuf[offsetSE] = shades[(packed >> 7) & 0x3F];
                }
                if ((flipflops & 2) != 0 && zPixel < zbuf[offsetSW]) {
                    zbuf[offsetSW] = zPixel;
                    pbuf[offsetSW] = shades[(packed >> 13) & 0x3F];
                }
                if ((flipflops & 4) != 0 && zPixel < zbuf[offsetNE]) {
                    zbuf[offsetNE] = zPixel;
                    pbuf[offsetNE] = shades[(packed >> 19) & 0x3F];
                }
                if ((flipflops & 8) != 0 && zPixel < zbuf[offsetNW]) {
                    zbuf[offsetNW] = zPixel;
                    pbuf[offsetNW] = shades[(packed >> 25) & 0x3F];
                }
                ++offsetSE;
                --offsetSW;
                ++offsetNE;
                --offsetNW;
                flipflops = ~flipflops;
            } while (packed >= 0);
            offsetSouthCenter += width;
            offsetNorthCenter -= width;
        } while (--nLines > 0);
    }
