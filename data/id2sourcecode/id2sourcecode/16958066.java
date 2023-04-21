    void buildHorizontalGridLevel(long c1, long c2, int depth) {
        long c = (c1 + c2) / 2;
        ZSegment s = new ZSegment(0, c, 0, HALF_WORLD_WIDTH, 0, GRID_COLOR);
        storeSegmentInHGrid(s, depth);
        vsm.addGlyph(s, mainVSname);
        s.setType(GLYPH_TYPE_GRID);
        s.setVisible(false);
        if (depth < GRID_DEPTH) {
            buildHorizontalGridLevel(c1, c, depth + 1);
            buildHorizontalGridLevel(c, c2, depth + 1);
        }
    }
