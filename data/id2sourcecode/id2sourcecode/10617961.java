    public boolean createTree(Tile[][] tiles, int x1, int x2, int y1, int y2) {
        if (x2 - x1 == 1) return true;
        children = new Quadtree[4];
        int midx = x1 + (x2 - x1) / 2, midy = y1 + (y2 - y1) / 2;
        children[0] = new Quadtree(tiles, x1, midx, y1, midy, this);
        children[1] = new Quadtree(tiles, midx, x2, y1, midy, this);
        children[2] = new Quadtree(tiles, midx, x2, midy, y2, this);
        children[3] = new Quadtree(tiles, x1, midx, midy, y2, this);
        return false;
    }
