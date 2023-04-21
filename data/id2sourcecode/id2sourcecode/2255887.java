    protected void analyzePixels() {
        int len = pixels.length;
        int nPix = len / 3;
        indexedPixels = new byte[nPix];
        if (gctused && (gct == null)) {
            NeuQuant nq = new NeuQuant(pixels, len, sample);
            colorTab = nq.process();
            gct = new byte[colorTab.length];
            for (int i = 0; i < colorTab.length; i += 3) {
                byte temp = colorTab[i];
                colorTab[i] = colorTab[i + 2];
                colorTab[i + 2] = temp;
                gct[i] = colorTab[i];
                gct[i + 1] = colorTab[i + 1];
                gct[i + 2] = colorTab[i + 2];
            }
            if (GCTextracted) {
                indexedPixels = null;
                return;
            }
        }
        if (!gctused) {
            NeuQuant nq = new NeuQuant(pixels, len, sample);
            colorTab = nq.process();
            for (int i = 0; i < colorTab.length; i += 3) {
                byte temp = colorTab[i];
                colorTab[i] = colorTab[i + 2];
                colorTab[i + 2] = temp;
            }
            int k = 0;
            for (int i = 0; i < nPix; i++) indexedPixels[i] = (byte) nq.map(pixels[k++] & 0xff, pixels[k++] & 0xff, pixels[k++] & 0xff);
            pixels = null;
            colorDepth = 8;
            lctSize = 7;
        }
        if (gctused) {
            colorTab = gct;
            int k = 0;
            int minpos;
            for (int j = 0; j < nPix; j++) {
                int b = pixels[k++] & 0xff;
                int g = pixels[k++] & 0xff;
                int r = pixels[k++] & 0xff;
                minpos = 0;
                int dmin = 256 * 256 * 256;
                int lenct = colorTab.length;
                for (int i = 0; i < lenct; ) {
                    int dr = r - (colorTab[i++] & 0xff);
                    int dg = g - (colorTab[i++] & 0xff);
                    int db = b - (colorTab[i] & 0xff);
                    int d = dr * dr + dg * dg + db * db;
                    if (d < dmin) {
                        dmin = d;
                        minpos = i / 3;
                    }
                    i++;
                }
                indexedPixels[j] = (byte) minpos;
            }
            pixels = null;
            colorDepth = 8;
            lctSize = 7;
        }
    }
