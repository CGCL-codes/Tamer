    void buildVerticalGridLevel(long c1, long c2, int depth) {
        long c = (c1 + c2) / 2;
        ZSegment s = new ZSegment(c, 0, 0, 0, HALF_WORLD_HEIGHT, GRID_COLOR);
        storeSegmentInVGrid(s, depth);
        vsm.addGlyph(s, mainVSname);
        s.setType(GLYPH_TYPE_GRID);
        s.setVisible(false);
        if (depth < GRID_DEPTH) {
            buildVerticalGridLevel(c1, c, depth + 1);
            buildVerticalGridLevel(c, c2, depth + 1);
        }
    }
