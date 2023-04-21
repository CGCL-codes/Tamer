    private void processColorCache(RdpPacket_Localised data) throws RdesktopException {
        byte[] palette = null;
        byte[] red = null;
        byte[] green = null;
        byte[] blue = null;
        int j = 0;
        int cache_id = data.get8();
        int n_colors = data.getLittleEndian16();
        palette = new byte[n_colors * 4];
        red = new byte[n_colors];
        green = new byte[n_colors];
        blue = new byte[n_colors];
        data.copyToByteArray(palette, 0, data.getPosition(), palette.length);
        data.incrementPosition(palette.length);
        for (int i = 0; i < n_colors; i++) {
            blue[i] = palette[j];
            green[i] = palette[j + 1];
            red[i] = palette[j + 2];
            j += 4;
        }
        IndexColorModel cm = new IndexColorModel(8, n_colors, red, green, blue);
        cache.put_colourmap(cache_id, cm);
    }
