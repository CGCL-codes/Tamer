    private void renderShapeClipped(int[] sphereShape) {
        int offsetSphere = 0;
        int evenSizeCorrection = 1 - (diameter & 1);
        int offsetSouthCenter = offsetPbufBeginLine;
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
                boolean isCore;
                int zOffset = packed & 0x7F;
                int zPixel;
                if (z < slab) {
                    zPixel = z + zOffset;
                    isCore = (zPixel >= slab);
                } else {
                    zPixel = zPixel(z, packed);
                    isCore = (zPixel < slab);
                }
                if (isCore) zPixel = slab;
                if (zPixel >= slab && zPixel <= depth) {
                    if (tSouthVisible) {
                        if (tEastVisible && (addAllPixels || (flipflops & 1) != 0) && zPixel < zbuf[offsetSE]) {
                            int i = (isCore ? SHADE_SLAB_CLIPPED - 3 + ((randu >> 7) & 0x07) : (packed >> 7) & 0x3F);
                            g3d.addPixel(offsetSE, zPixel, shades[i >> zShift]);
                        }
                        if (tWestVisible && (addAllPixels || (flipflops & 2) != 0) && zPixel < zbuf[offsetSW]) {
                            int i = (isCore ? SHADE_SLAB_CLIPPED - 3 + ((randu >> 13) & 0x07) : (packed >> 13) & 0x3F);
                            g3d.addPixel(offsetSW, zPixel, shades[i >> zShift]);
                        }
                    }
                    if (tNorthVisible) {
                        if (tEastVisible && (!tScreened || (flipflops & 4) != 0) && zPixel < zbuf[offsetNE]) {
                            int i = (isCore ? SHADE_SLAB_CLIPPED - 3 + ((randu >> 19) & 0x07) : (packed >> 19) & 0x3F);
                            g3d.addPixel(offsetNE, zPixel, shades[i >> zShift]);
                        }
                        if (tWestVisible && (!tScreened || (flipflops & 8) != 0) && zPixel < zbuf[offsetNW]) {
                            int i = (isCore ? SHADE_SLAB_CLIPPED - 3 + ((randu >> 25) & 0x07) : (packed >> 25) & 0x3F);
                            g3d.addPixel(offsetNW, zPixel, shades[i >> zShift]);
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
                if (isCore) randu = ((randu << 16) + (randu << 1) + randu) & 0x7FFFFFFF;
            } while (packed >= 0);
            offsetSouthCenter += width;
            offsetNorthCenter -= width;
            ++ySouth;
            --yNorth;
        } while (--nLines > 0);
    }
