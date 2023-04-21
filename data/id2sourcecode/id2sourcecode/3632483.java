    public void readImage(PsdInputStream input, boolean needReadPlaneInfo, short[] lineLengths) throws IOException {
        byte[] r = null, g = null, b = null, a = null;
        for (int j = 0; j < numberOfChannels; j++) {
            int id = channelsInfo.get(j).getId();
            switch(id) {
                case 0:
                    r = readPlane(input, getWidth(), getHeight(), lineLengths, needReadPlaneInfo, j);
                    break;
                case 1:
                    g = readPlane(input, getWidth(), getHeight(), lineLengths, needReadPlaneInfo, j);
                    break;
                case 2:
                    b = readPlane(input, getWidth(), getHeight(), lineLengths, needReadPlaneInfo, j);
                    break;
                case -1:
                    a = readPlane(input, getWidth(), getHeight(), lineLengths, needReadPlaneInfo, j);
                    if (this.opacity != -1) {
                        double opacity = (this.opacity & 0xff) / 256d;
                        for (int i = 0; i < a.length; i++) {
                            a[i] = (byte) ((a[i] & 0xff) * opacity);
                        }
                    }
                    break;
                default:
                    input.skipBytes(getChannelInfoById(id).getDataLength());
            }
        }
        int n = getWidth() * getHeight();
        if (r == null) r = fillBytes(n, 0);
        if (g == null) g = fillBytes(n, 0);
        if (b == null) b = fillBytes(n, 0);
        if (a == null) a = fillBytes(n, 255);
        imageData = makeSWTImage(getWidth(), getHeight(), r, g, b, a);
    }
