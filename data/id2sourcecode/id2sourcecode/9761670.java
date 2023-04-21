    int[] createSphereShape(int diameter) {
        int countSE = 0;
        boolean oddDiameter = (diameter & 1) != 0;
        float radiusF = diameter / 2.0f;
        float radiusF2 = radiusF * radiusF;
        int radius = (diameter + 1) / 2;
        float y = oddDiameter ? 0 : 0.5f;
        for (int i = 0; i < radius; ++i, ++y) {
            float y2 = y * y;
            float x = oddDiameter ? 0 : 0.5f;
            for (int j = 0; j < radius; ++j, ++x) {
                float x2 = x * x;
                float z2 = radiusF2 - y2 - x2;
                if (z2 >= 0) ++countSE;
            }
        }
        int[] sphereShape = new int[countSE];
        int offset = 0;
        y = oddDiameter ? 0 : 0.5f;
        for (int i = 0; i < radius; ++i, ++y) {
            float y2 = y * y;
            float x = oddDiameter ? 0 : 0.5f;
            for (int j = 0; j < radius; ++j, ++x) {
                float x2 = x * x;
                float z2 = radiusF2 - y2 - x2;
                if (z2 >= 0) {
                    float z = (float) Math.sqrt(z2);
                    int height = (int) z;
                    int intensitySE = Shade3D.calcDitheredNoisyIntensity(x, y, z, radiusF);
                    int intensitySW = Shade3D.calcDitheredNoisyIntensity(-x, y, z, radiusF);
                    int intensityNE = Shade3D.calcDitheredNoisyIntensity(x, -y, z, radiusF);
                    int intensityNW = Shade3D.calcDitheredNoisyIntensity(-x, -y, z, radiusF);
                    int packed = (height | (intensitySE << 7) | (intensitySW << 13) | (intensityNE << 19) | (intensityNW << 25));
                    sphereShape[offset++] = packed;
                }
            }
            sphereShape[offset - 1] |= 0x80000000;
        }
        return sphereShape;
    }
