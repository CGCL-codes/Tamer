    protected void processPalette(RdpPacket_Localised data) {
        int n_colors = 0;
        byte[] palette = null;
        byte[] red = null;
        byte[] green = null;
        byte[] blue = null;
        int j = 0;
        data.incrementPosition(2);
        n_colors = data.getLittleEndian16();
        data.incrementPosition(2);
        palette = new byte[n_colors * 3];
        red = new byte[n_colors];
        green = new byte[n_colors];
        blue = new byte[n_colors];
        data.copyToByteArray(palette, 0, data.getPosition(), palette.length);
        data.incrementPosition(palette.length);
        for (int i = 0; i < n_colors; i++) {
            red[i] = palette[j];
            green[i] = palette[j + 1];
            blue[i] = palette[j + 2];
            j += 3;
        }
    }
