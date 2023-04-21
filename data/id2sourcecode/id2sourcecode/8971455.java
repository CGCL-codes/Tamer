    private static Image sphereSetup(Component component, Color ballColor, int diameter, double[] lightSource) {
        double v1[] = new double[3];
        double v2[] = new double[3];
        int radius = (diameter + 1) / 2;
        int j = -1;
        int model[] = new int[diameter * diameter];
        double[] lightsource = new double[3];
        for (int i = 0; i < 3; ++i) lightsource[i] = lightSource[i];
        normalize(lightsource);
        for (int k1 = -(diameter - radius); k1 < radius; k1++) {
            for (int k2 = -(diameter - radius); k2 < radius; k2++) {
                j++;
                v1[0] = k2;
                v1[1] = k1;
                double len1 = Math.sqrt(k2 * k2 + k1 * k1);
                if (len1 <= radius) {
                    int red2 = 0;
                    int green2 = 0;
                    int blue2 = 0;
                    v1[2] = radius * Math.cos(Math.asin(len1 / radius));
                    normalize(v1);
                    double len2 = Math.abs((v1[0] * lightsource[0] + v1[1] * lightsource[1] + v1[2] * lightsource[2]));
                    if (len2 < 0.995f) {
                        red2 = (int) (ballColor.getRed() * len2);
                        green2 = (int) (ballColor.getGreen() * len2);
                        blue2 = (int) (ballColor.getBlue() * len2);
                    } else {
                        v2[0] = lightsource[0] + 0.0f;
                        v2[1] = lightsource[1] + 0.0f;
                        v2[2] = lightsource[2] + 1.0f;
                        normalize(v2);
                        double len3 = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
                        double len4 = 8.0f * len3 * len3 - 7.0f;
                        double len5 = 100.0f * len4;
                        len5 = Math.max(len5, 0.0f);
                        red2 = (int) (ballColor.getRed() * 155 * len2 + 100.0 + len5);
                        green2 = (int) (ballColor.getGreen() * 155 * len2 + 100.0 + len5);
                        blue2 = (int) (ballColor.getBlue() * 155 * len2 + 100.0 + len5);
                    }
                    red2 = Math.min(red2 + 32, 255);
                    green2 = Math.min(green2 + 32, 255);
                    blue2 = Math.min(blue2 + 32, 255);
                    model[j] = 0xff000000 | red2 << 16 | green2 << 8 | blue2;
                } else {
                    model[j] = 0x00000000;
                }
            }
        }
        return component.createImage(new MemoryImageSource(diameter, diameter, model, 0, diameter));
    }
