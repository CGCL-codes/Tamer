    private String makeRemoteSatName(int x, int y, int zoom) {
        int ServerNumber = (int) Math.round(Math.random() * 3.0);
        char[] sat = new char[20];
        int i = 1;
        int curZoom = 16 - (zoom - 1);
        int midxTiles = (int) Math.pow(2, (curZoom - 1));
        int midyTiles = midxTiles;
        int maxxTiles = midxTiles * 2;
        int maxyTiles = midyTiles * 2;
        int minxTiles = 0;
        int minyTiles = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("t");
        BufferedImage currentImage;
        while (curZoom != 0) {
            if (x >= 0 && y >= 0) {
                if (x >= midxTiles) {
                    minxTiles = midxTiles;
                    if (y >= midyTiles) {
                        sat[i] = 's';
                        minyTiles = midyTiles;
                    } else {
                        sat[i] = 'r';
                        maxyTiles = midyTiles;
                    }
                } else if (x < midxTiles) {
                    maxxTiles = midxTiles;
                    if (y >= midyTiles) {
                        sat[i] = 't';
                        minyTiles = midyTiles;
                    } else {
                        sat[i] = 'q';
                        maxyTiles = midyTiles;
                    }
                }
                midxTiles = minxTiles + (maxxTiles - minxTiles) / 2;
                midyTiles = minyTiles + (maxyTiles - minyTiles) / 2;
                String s = new Character(sat[i]).toString();
                sb.append(s);
                i++;
                curZoom--;
            } else {
            }
        }
        String satImage = sb.toString();
        return satImage;
    }
