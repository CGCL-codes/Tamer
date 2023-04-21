    void renderShapeClipped(int[] shades, boolean tScreened, int[] sphereShape, int diameter, int x, int y, int z) {
        int[] pbuf = g3d.pbuf;
        int[] zbuf = g3d.zbuf;
        int offsetSphere = 0;
        int width = g3d.width, height = g3d.height;
        int slab = g3d.slab, depth = g3d.depth;
        int evenSizeCorrection = 1 - (diameter & 1);
        int offsetSouthCenter = width * y + x;
        int offsetNorthCenter = offsetSouthCenter - evenSizeCorrection * width;
        int nLines = (diameter + 1) / 2;
        int ySouth = y;
        int yNorth = y - evenSizeCorrection;
        int randu = (x << 16) + (y << 1) ^ 0x33333333;
        int flipflopSouthCenter = (x ^ y) & 1;
        int flipflopNorthCenter = flipflopSouthCenter ^ evenSizeCorrection;
        int flipflopSE = flipflopSouthCenter;
        int flipflopSW = flipflopSouthCenter ^ evenSizeCorrection;
        int flipflopNE = flipflopNorthCenter;
        int flipflopNW = flipflopNorthCenter ^ evenSizeCorrection;
        int flipflopsCenter = flipflopSE | (flipflopSW << 1) | (flipflopNE << 2) | (flipflopNW << 3);
        do {
            boolean tSouthVisible = ySouth >= 0 && ySouth < height;
            boolean tNorthVisible = yNorth >= 0 && yNorth < height;
            int offsetSE = offsetSouthCenter;
            int offsetSW = offsetSouthCenter - evenSizeCorrection;
            int offsetNE = offsetNorthCenter;
            int offsetNW = offsetNorthCenter - evenSizeCorrection;
            int packed;
            int flipflops = (flipflopsCenter = ~flipflopsCenter);
            int xEast = x;
            int xWest = x - evenSizeCorrection;
            do {
                boolean tWestVisible = xWest >= 0 && xWest < width;
                boolean tEastVisible = xEast >= 0 && xEast < width;
                packed = sphereShape[offsetSphere++];
                int zPixel;
                boolean isSlabClipped;
                if (z < slab) {
                    zPixel = z + (packed & 0x7F);
                    isSlabClipped = zPixel > slab;
                } else {
                    zPixel = z - (packed & 0x7F);
                    isSlabClipped = zPixel < slab;
                }
                if (isSlabClipped) zPixel = slab;
                if (zPixel >= slab && zPixel <= depth) {
                    if (tSouthVisible) {
                        if (tEastVisible && (!tScreened || (flipflops & 1) != 0) && zPixel < zbuf[offsetSE]) {
                            zbuf[offsetSE] = zPixel;
                            int i = (packed >> 7) & 0x3F;
                            if (isSlabClipped) i = SHADE_SLAB_CLIPPED - 3 + ((randu >> 7) & 0x07);
                            pbuf[offsetSE] = shades[i];
                        }
                        if (tWestVisible && (!tScreened || (flipflops & 2) != 0) && zPixel < zbuf[offsetSW]) {
                            zbuf[offsetSW] = zPixel;
                            int i = (packed >> 13) & 0x3F;
                            if (isSlabClipped) i = SHADE_SLAB_CLIPPED - 3 + ((randu >> 13) & 0x07);
                            pbuf[offsetSW] = shades[i];
                        }
                    }
                    if (tNorthVisible) {
                        if (tEastVisible && (!tScreened || (flipflops & 4) != 0) && zPixel < zbuf[offsetNE]) {
                            zbuf[offsetNE] = zPixel;
                            int i = (packed >> 19) & 0x3F;
                            if (isSlabClipped) i = SHADE_SLAB_CLIPPED - 3 + ((randu >> 19) & 0x07);
                            pbuf[offsetNE] = shades[i];
                        }
                        if (tWestVisible && (!tScreened || (flipflops & 8) != 0) && zPixel < zbuf[offsetNW]) {
                            zbuf[offsetNW] = zPixel;
                            int i = (packed >> 25) & 0x3F;
                            if (isSlabClipped) i = SHADE_SLAB_CLIPPED - 3 + ((randu >> 25) & 0x07);
                            pbuf[offsetNW] = shades[i];
                        }
                    }
                }
                ++offsetSE;
                --offsetSW;
                ++offsetNE;
                --offsetNW;
                ++xEast;
                --xWest;
                flipflops = ~flipflops;
                if (isSlabClipped) randu = ((randu << 16) + (randu << 1) + randu) & 0x7FFFFFFF;
            } while (packed >= 0);
            offsetSouthCenter += width;
            offsetNorthCenter -= width;
            ++ySouth;
            --yNorth;
        } while (--nLines > 0);
    }
