    public void addPixels(int nArgs, FtsAtom args[]) {
        int startIndex = args[0].intValue;
        int i = 0;
        int j = 0;
        int newp = (int) (nArgs - 1) / 2;
        double[] t_temp = new double[pixelsSize + newp];
        double[] b_temp = new double[pixelsSize + newp];
        if (startIndex == 0) {
            if (isIvec()) {
                for (i = 0; i < (nArgs - 1); i += 2) {
                    t_temp[j] = (double) args[i + 1].intValue;
                    b_temp[j] = (double) args[i + 2].intValue;
                    j++;
                }
            } else {
                for (i = 0; i < (nArgs - 1); i += 2) {
                    t_temp[j] = args[i + 1].doubleValue;
                    b_temp[j] = args[i + 2].doubleValue;
                    j++;
                }
            }
            for (i = newp; i < pixelsSize; i++) {
                t_temp[i] = t_pixels[i - newp];
                b_temp[i] = b_pixels[i - newp];
            }
        } else {
            for (i = 0; (i < pixelsSize - newp) && (i + newp < t_temp.length); i++) {
                t_temp[i] = t_pixels[i + newp];
                b_temp[i] = b_pixels[i + newp];
            }
            j = 1;
            if (isIvec()) for (i = 1; i <= (nArgs - 1); i += 2) {
                t_temp[pixelsSize - newp - 1 + j] = (double) args[i].intValue;
                b_temp[pixelsSize - newp - 1 + j] = (double) args[i + 1].intValue;
                j++;
            } else for (i = 1; (i <= (nArgs - 2)) && (pixelsSize - newp - 1 + j < t_temp.length); i += 2) {
                t_temp[pixelsSize - newp - 1 + j] = args[i].doubleValue;
                b_temp[pixelsSize - newp - 1 + j] = args[i + 1].doubleValue;
                j++;
            }
        }
        t_pixels = t_temp;
        b_pixels = b_temp;
        notifySet();
    }
