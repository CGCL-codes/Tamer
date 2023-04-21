    private void renderShapeUnclipped(int[] sphereShape) {
        int offsetSphere = 0;
        int evenSizeCorrection = 1 - (diameter & 1);
        int offsetSouthCenter = offsetPbufBeginLine;
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
                    int zPixel = zPixel(z, packed);
                    if (zPixel < zbuf[offsetSE]) g3d.addPixel(offsetSE, zPixel, shades[((packed >> 7) & 0x3F) >> zShift]);
                    if (zPixel < zbuf[offsetSW]) g3d.addPixel(offsetSW, zPixel, shades[((packed >> 13) & 0x3F) >> zShift]);
                    if (zPixel < zbuf[offsetNE]) g3d.addPixel(offsetNE, zPixel, shades[((packed >> 19) & 0x3F) >> zShift]);
                    if (zPixel < zbuf[offsetNW]) g3d.addPixel(offsetNW, zPixel, shades[((packed >> 25) & 0x3F) >> zShift]);
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
                if ((flipflops & 1) != 0 && zPixel < zbuf[offsetSE]) g3d.addPixel(offsetSE, zPixel, shades[((packed >> 7) & 0x3F) >> zShift]);
                if ((flipflops & 2) != 0 && zPixel < zbuf[offsetSW]) g3d.addPixel(offsetSW, zPixel, shades[((packed >> 13) & 0x3F) >> zShift]);
                if ((flipflops & 4) != 0 && zPixel < zbuf[offsetNE]) g3d.addPixel(offsetNE, zPixel, shades[((packed >> 19) & 0x3F) >> zShift]);
                if ((flipflops & 8) != 0 && zPixel < zbuf[offsetNW]) g3d.addPixel(offsetNW, zPixel, shades[((packed >> 25) & 0x3F) >> zShift]);
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
