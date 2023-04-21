    public static void compressImage(int[] pixels, int width, int height, OutputStream pout) throws IOException {
        ByteArrayOutputStream moutP = new ByteArrayOutputStream(1024);
        ByteArrayOutputStream moutNP = new ByteArrayOutputStream(1024);
        ByteArrayOutputStream moutP8 = new ByteArrayOutputStream(1024);
        ByteArrayOutputStream moutNP8 = new ByteArrayOutputStream(1024);
        Map<Integer, Integer> colorToIndex = new HashMap<Integer, Integer>();
        Map<Integer, Integer> indexToColor = new HashMap<Integer, Integer>();
        int colorIndex = 0;
        boolean transparency = false;
        boolean morealpha = false;
        for (int i = 0; i < pixels.length; i++) {
            int c = pixels[i];
            int a = (c >> 24) & 0xFF;
            if (a < 255) {
                transparency = true;
                if (a > 0) {
                    morealpha = true;
                    if (!colorToIndex.containsKey(c)) {
                        colorToIndex.put(c, colorIndex);
                        indexToColor.put(colorIndex, c);
                        colorIndex++;
                    }
                }
            } else {
                if (!colorToIndex.containsKey(c)) {
                    colorToIndex.put(c, colorIndex);
                    indexToColor.put(colorIndex, c);
                    colorIndex++;
                }
            }
        }
        if (transparency && !morealpha) {
            colorToIndex.put(0, colorIndex);
            colorIndex++;
        }
        int bpp = 32 - Integer.numberOfLeadingZeros(colorIndex);
        writeImageData(width, height, moutP, colorToIndex, indexToColor, pixels, colorIndex, transparency, morealpha, bpp, true);
        writeImageData(width, height, moutNP, colorToIndex, indexToColor, pixels, colorIndex, transparency, morealpha, bpp, false);
        if (bpp <= 8) {
            writeImageData(width, height, moutP8, colorToIndex, indexToColor, pixels, colorIndex, transparency, morealpha, 8, true);
            writeImageData(width, height, moutNP8, colorToIndex, indexToColor, pixels, colorIndex, transparency, morealpha, 8, false);
        }
        char type = 'R';
        ByteArrayOutputStream mout = null;
        for (ByteArrayOutputStream raw : new ByteArrayOutputStream[] { moutP, moutP8, moutNP, moutNP8 }) {
            if (raw.size() > 0) {
                if (mout == null || mout.size() > raw.size()) {
                    type = 'R';
                    mout = raw;
                }
                ByteArrayOutputStream gz = new ByteArrayOutputStream(1024);
                GZIPOutputStream gzout = new GZIPOutputStream(gz);
                raw.writeTo(gzout);
                gzout.close();
                if (mout.size() > gz.size()) {
                    mout = gz;
                    type = 'G';
                }
                ByteArrayOutputStream zip = new ByteArrayOutputStream(1024);
                ZipOutputStream zout = new ZipOutputStream(zip);
                zout.setLevel(9);
                zout.putNextEntry(new ZipEntry("raw.img"));
                raw.writeTo(zout);
                zout.close();
                if (mout.size() > zip.size()) {
                    mout = zip;
                    type = 'Z';
                }
            }
        }
        pout.write(type);
        mout.writeTo(pout);
        pout.flush();
    }
