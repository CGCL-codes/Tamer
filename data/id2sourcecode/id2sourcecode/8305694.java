    public void addRandomRows(final int count) {
        Row r;
        for (int i = 0; i < count; i++) {
            r = new Row(MebisScreen.ROWS - 1, MebisScreen.COLS);
            for (int z = 0; z < r.blocks.length; z++) {
                int rr = BaseApp.rand(6);
                if (rr != 0) {
                    rr = BaseApp.rand(7);
                    final int color = Brick.colors[rr];
                    r.blocks[z] = new Block(color, z, MebisScreen.ROWS - 1);
                }
            }
            for (int y = 0; y < MebisScreen.ROWS - 1; y++) {
                rows[y] = rows[y + 1];
            }
            rows[MebisScreen.ROWS - 1] = r;
        }
    }
