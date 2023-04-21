    public void addRandomRows(int count) {
        Row r;
        for (int i = 0; i < count; i++) {
            r = new Row(ROWS - 1);
            for (int z = 0; z < r.blocks.length; z++) {
                if (Math.abs((int) rand.nextLong() % 6) != 0) {
                    int color = Brick.colors[Math.abs((int) rand.nextLong() % 7)];
                    r.blocks[z] = new Block(color, z, ROWS - 1);
                }
            }
            for (int y = 0; y < ROWS - 1; y++) {
                rows[y] = rows[y + 1];
            }
            rows[ROWS - 1] = r;
        }
        repaint();
    }
