    public static BufferedImage convertToIndexed(BufferedImage image, int paletteSize, Color transparent) {
        boolean isIndexed = image.getColorModel() instanceof IndexColorModel;
        BufferedImage result = image;
        if (!isIndexed || (((IndexColorModel) image.getColorModel()).getMapSize() > paletteSize)) {
            BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D gr = temp.createGraphics();
            gr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            gr.drawImage(image, 0, 0, null);
            byte[] pixels = ((DataBufferByte) temp.getRaster().getDataBuffer()).getData();
            int nPix = pixels.length / 3;
            int[] indexedPixels = new int[nPix];
            NeuQuant nq = new NeuQuant(pixels, pixels.length, 10, paletteSize);
            gr.dispose();
            byte[] colorTab = nq.process();
            boolean[] usedEntry = new boolean[paletteSize];
            for (int i = 0; i < colorTab.length; i += 3) {
                byte b = colorTab[i];
                colorTab[i] = colorTab[i + 2];
                colorTab[i + 2] = b;
                usedEntry[i / 3] = false;
            }
            int k = 0;
            for (int i = 0; i < nPix; i++) {
                int index = nq.map(pixels[k++] & 0xff, pixels[k++] & 0xff, pixels[k++] & 0xff);
                usedEntry[index] = true;
                indexedPixels[i] = (byte) index;
            }
            int bits = (int) Math.ceil(Math.log(paletteSize) / Math.log(2));
            int trans = -1;
            if (isIndexed && (transparent == null)) {
                IndexColorModel icm = (IndexColorModel) image.getColorModel();
                transparent = new Color(icm.getRGB(icm.getTransparentPixel()));
            }
            if (transparent != null) {
                int r = transparent.getRed();
                int g = transparent.getGreen();
                int b = transparent.getBlue();
                int dmin = 256 * 256 * 256;
                int len = colorTab.length;
                for (int i = 0; i < len; ) {
                    int dr = r - (colorTab[i++] & 0xff);
                    int dg = g - (colorTab[i++] & 0xff);
                    int db = b - (colorTab[i] & 0xff);
                    int d = dr * dr + dg * dg + db * db;
                    int index = i / 3;
                    if (usedEntry[index] && (d < dmin)) {
                        dmin = d;
                        trans = index;
                    }
                    i++;
                }
            }
            IndexColorModel model = new IndexColorModel(bits, colorTab.length / 3, colorTab, 0, false, trans);
            Color t = null;
            if (model.getTransparentPixel() >= 0) {
                t = new Color(model.getRGB(model.getTransparentPixel()));
            }
            image.flush();
            result = new BufferedImage(image.getWidth(), image.getHeight(), image.TYPE_BYTE_INDEXED, model);
            result.getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), indexedPixels);
        }
        return result;
    }
