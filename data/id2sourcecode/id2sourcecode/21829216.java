    public static int[] getSquareKernelnoCenter(int kernellength, int kernelwidth, int imagewidth) {
        int[] offset = new int[kernellength];
        int center = kernellength / 2 + 1;
        for (int i = 0; i < kernelwidth; i++) {
            for (int j = 0; j < kernelwidth; j++) {
                if (center == i && center == j) {
                    continue;
                }
                offset[j + kernelwidth * i] = (imagewidth * i + j) * 4;
            }
        }
        int[] offset2 = new int[kernellength - 1];
        for (int i = 0; i < offset2.length / 2; i++) {
            offset2[i] = offset[i];
        }
        for (int i = offset2.length / 2; i < offset2.length; i++) {
            offset2[i] = offset[i + 1];
        }
        return offset2;
    }
