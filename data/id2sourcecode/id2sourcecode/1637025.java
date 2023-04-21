    private float[] oddFiltering(float[] src, int z) throws ErrorException {
        int subbandSize = src.length;
        int half = subbandSize / 2;
        float dst[] = new float[subbandSize];
        for (int k = 0; k < half; k++) {
            dst[2 * k] = src[k];
            dst[2 * k + 1] = src[half + k + 1];
        }
        dst[subbandSize - 1] = src[half];
        if (WTTypes[z] == 1) {
            dst[0] = dst[0] - (float) (Math.floor(((dst[1] + dst[1] + 2) / 4)));
            for (int k = 2; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - (float) (Math.floor(((dst[k - 1] + dst[k + 1] + 2) / 4)));
            }
            dst[subbandSize - 1] = dst[subbandSize - 1] - (float) (Math.floor((dst[subbandSize - 2] + dst[subbandSize - 2] + 2) / 4));
            for (int k = 1; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] + (float) (Math.floor(((dst[k - 1] + dst[k + 1]) / 2)));
            }
        } else if (WTTypes[z] == 2 || WTTypes[z] == 3) {
            final float alfa_97 = -1.586134342059924F;
            final float beta_97 = -0.052980118572961F;
            final float gamma_97 = 0.882911075530934F;
            final float delta_97 = 0.443506852043971F;
            final float nh_97, nl_97;
            if (WTTypes[z] == 2) {
                nh_97 = 1.230174104914001F;
                nl_97 = 1F / nh_97;
            } else {
                nh_97 = 1.14960430535816F;
                nl_97 = -1F / nh_97;
            }
            for (int k = 0; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] / nl_97;
                dst[k + 1] = dst[k + 1] / nh_97;
            }
            dst[subbandSize - 1] = dst[subbandSize - 1] / nl_97;
            dst[0] = dst[0] - delta_97 * (dst[1] + dst[1]);
            for (int k = 2; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - delta_97 * (dst[k - 1] + dst[k + 1]);
            }
            dst[subbandSize - 1] = dst[subbandSize - 1] - delta_97 * (dst[subbandSize - 2] + dst[subbandSize - 2]);
            for (int k = 1; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - gamma_97 * (dst[k - 1] + dst[k + 1]);
            }
            dst[0] = dst[0] - beta_97 * (dst[1] + dst[1]);
            for (int k = 2; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - beta_97 * (dst[k - 1] + dst[k + 1]);
            }
            dst[subbandSize - 1] = dst[subbandSize - 1] - beta_97 * (dst[subbandSize - 2] + dst[subbandSize - 2]);
            for (int k = 1; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - alfa_97 * (dst[k - 1] + dst[k + 1]);
            }
        } else if (WTTypes[z] == 4) {
            throw new ErrorException("Integer 9/7M CCSDS Recommended is not implemented for odd signals.!!!");
        } else if (WTTypes[z] == 5 || WTTypes[z] == 6) {
            final float alfa, beta, gamma, delta;
            if (WTTypes[z] == 6) {
                alfa = -1.58615986717275F;
                beta = -0.05297864003258F;
                gamma = 0.88293362717904F;
                delta = 0.44350482244527F;
            } else {
                alfa = -0.5F;
                beta = 0.25F;
                gamma = 0.F;
                delta = 0.F;
            }
            if (WTTypes[z] == 6) {
                dst[0] = dst[0] - (float) Math.floor(delta * (dst[1] + dst[1]) + 0.5);
                for (int k = 2; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - (float) Math.floor(delta * (dst[k - 1] + dst[k + 1]) + 0.5);
                }
                dst[subbandSize - 1] = dst[subbandSize - 1] - (float) Math.floor(delta * (dst[subbandSize - 2] + dst[subbandSize - 2]) + 0.5);
                for (int k = 1; k < subbandSize - 1; k += 2) {
                    dst[k] = dst[k] - (float) Math.floor(gamma * (dst[k - 1] + dst[k + 1]) + 0.5);
                }
            }
            dst[0] = dst[0] - (float) Math.floor(beta * (dst[1] + dst[1]) + 0.5);
            for (int k = 2; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - (float) Math.floor(beta * (dst[k - 1] + dst[k + 1]) + 0.5);
            }
            dst[subbandSize - 1] = dst[subbandSize - 1] - (float) Math.floor(beta * (dst[subbandSize - 2] + dst[subbandSize - 2]) + 0.5);
            for (int k = 1; k < subbandSize - 1; k += 2) {
                dst[k] = dst[k] - (float) Math.floor(alfa * (dst[k - 1] + dst[k + 1]) + 0.5);
            }
        } else if (WTTypes[z] == 7) {
            float sample1 = 0, sample2 = 0;
            float normFactor = (float) (Math.sqrt(2));
            for (int k = 0; k < subbandSize - 1; k += 2) {
                sample1 = dst[k] + dst[k + 1];
                sample2 = dst[k] - dst[k + 1];
                dst[k] = sample1 * normFactor;
                dst[k + 1] = sample2 * normFactor;
            }
            dst[subbandSize - 1] = dst[subbandSize - 1];
        } else if (WTTypes[z] == 8) {
            float s = 0;
            for (int k = 0; k < subbandSize - 1; k += 2) {
                s = dst[k] - (float) Math.floor(dst[k + 1] / 2);
                dst[k] = dst[k + 1] + s;
                dst[k + 1] = s;
            }
            dst[subbandSize - 1] = dst[subbandSize - 1];
        } else {
            throw new ErrorException("Unrecognized wavelet transform type.");
        }
        return (dst);
    }
