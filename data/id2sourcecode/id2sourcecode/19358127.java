    private float[] oddOddFiltering(float[] src, int WTTypes) throws ErrorException {
        int subbandSize = src.length;
        int half = subbandSize / 2;
        float dst[] = new float[subbandSize];
        for (int k = 0; k < half; k++) {
            dst[2 * k] = src[k];
            dst[2 * k + 1] = src[half + k + 1];
        }
        dst[subbandSize - 1] = src[half];
        switch(WTTypes) {
            case 1:
                for (int k = 1; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - (float) (Math.floor(((dst[k - 1] + dst[k + 1] + 2) / 4)));
                }
                for (int k = 2; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] + (float) (Math.floor(((dst[k - 1] + dst[k + 1]) / 2)));
                }
                dst[0] = dst[0] + (float) (Math.floor(((dst[1] + dst[1]) / 2)));
                dst[subbandSize - 1] = dst[subbandSize - 1] + (float) (Math.floor(((dst[subbandSize - 2] + dst[subbandSize - 2]) / 2)));
                break;
            case 2:
            case 3:
                final float alfa_97 = -1.586134342059924F;
                final float beta_97 = -0.052980118572961F;
                final float gamma_97 = 0.882911075530934F;
                final float delta_97 = 0.443506852043971F;
                final float nh_97, nl_97;
                if (WTTypes == 2) {
                    nh_97 = 1.230174104914001F;
                    nl_97 = 1F / nh_97;
                } else {
                    nl_97 = 1.14960430535816F;
                    nh_97 = -1F / nl_97;
                }
                for (int k = 0; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] / nh_97;
                    dst[k + 1] = dst[k + 1] / nl_97;
                }
                dst[subbandSize - 1] = dst[subbandSize - 1] / nh_97;
                for (int k = 1; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - delta_97 * (dst[k - 1] + dst[k + 1]);
                }
                dst[subbandSize - 1] = dst[subbandSize - 1] - gamma_97 * (dst[subbandSize - 2] + dst[subbandSize - 2]);
                dst[0] = dst[0] - gamma_97 * (dst[1] + dst[1]);
                for (int k = 2; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - gamma_97 * (dst[k - 1] + dst[k + 1]);
                }
                for (int k = 1; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - beta_97 * (dst[k - 1] + dst[k + 1]);
                }
                dst[subbandSize - 1] = dst[subbandSize - 1] - alfa_97 * (dst[subbandSize - 2] + dst[subbandSize - 2]);
                dst[0] = dst[0] - alfa_97 * (dst[1] + dst[1]);
                for (int k = 2; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - alfa_97 * (dst[k - 1] + dst[k + 1]);
                }
                break;
            case 4:
                if (subbandSize >= 6) {
                    final float alfa1 = (9F / 16F);
                    final float alfa2 = (1F / 16F);
                    final float beta = (1F / 4F);
                    for (int k = 1; k < subbandSize; k += 2) {
                        dst[k] = dst[k] + (float) (Math.floor(-beta * (dst[k - 1] + dst[k + 1]) + 0.5));
                    }
                    dst[0] = dst[0] + (float) (Math.floor(alfa1 * (dst[1] + dst[1]) - alfa2 * (dst[3] + dst[3]) + 0.5));
                    dst[2] = dst[2] + (float) (Math.floor(alfa1 * (dst[1] + dst[3]) - alfa2 * (dst[1] + dst[5]) + 0.5));
                    for (int k = 4; k < subbandSize - 3; k += 2) {
                        dst[k] = dst[k] + (float) (Math.floor(alfa1 * (dst[k - 1] + dst[k + 1]) - alfa2 * (dst[k - 3] + dst[k + 3]) + 0.5));
                    }
                    dst[subbandSize - 3] = dst[subbandSize - 3] + (float) (Math.floor(alfa1 * (dst[subbandSize - 4] + dst[subbandSize - 2]) - alfa2 * (dst[subbandSize - 6] + dst[subbandSize - 2]) + 0.5));
                    dst[subbandSize - 1] = dst[subbandSize - 1] + (float) (Math.floor(alfa1 * (dst[subbandSize - 2] + dst[subbandSize - 2]) - alfa2 * (dst[subbandSize - 4] + dst[subbandSize - 4]) + 0.5));
                } else {
                    throw new ErrorException("Size should be greater or equal than 6 in order to perform 9/7M");
                }
                break;
            case 7:
                float sample1 = 0, sample2 = 0;
                float normFactor = (float) (Math.sqrt(2));
                for (int k = 0; k < subbandSize - 1; k += 2) {
                    sample1 = dst[k] + dst[k + 1];
                    sample2 = dst[k] - dst[k + 1];
                    dst[k] = sample1 * normFactor;
                    dst[k + 1] = sample2 * normFactor;
                }
                dst[subbandSize - 1] = dst[subbandSize - 1];
                break;
            case 8:
                float s = 0;
                for (int k = 0; k < subbandSize - 1; k += 2) {
                    s = dst[k] - (float) Math.floor(dst[k + 1] / 2);
                    dst[k] = dst[k + 1] + s;
                    dst[k + 1] = s;
                }
                dst[subbandSize - 1] = dst[subbandSize - 1];
                break;
            default:
                throw new ErrorException("Unrecognized wavelet transform type.");
        }
        return (dst);
    }
