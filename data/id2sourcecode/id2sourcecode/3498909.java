    static final void PutPix2(int x, int y, int p1, int p2) {
        CharPtr p = new CharPtr(williams_bitmap.line[x], y);
        CharPtr lookup = new CharPtr(Machine.gfx[0].colortable, 0);
        p.write(0, lookup.read(p1));
        p.write(1, lookup.read(p2));
    }
