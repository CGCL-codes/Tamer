    public Neighbor getNeighbor(int x, int y, int channel, int type) {
        Neighbor nbor = new Neighbor(type);
        int offset = nbor.getOffset();
        int ulx = x - offset;
        int uly = y - offset;
        for (int i = (y - offset); i <= y + offset; i++) {
            for (int j = (x - offset); j <= x + offset; j++) {
                RGBColor rgb = this.getRGBColor(j, i);
                nbor.setValue(j - ulx, i - uly, rgb.getChannel(channel));
            }
        }
        return nbor;
    }
