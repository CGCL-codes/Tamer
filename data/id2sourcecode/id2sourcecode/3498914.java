    static void williams_videoram_blitter_w(int offset, int data, int flag, int p1, int p2, char[] vram) {
        int x, y;
        int pb1, pb2;
        CharPtr bm = new CharPtr();
        x = offset % 256;
        y = offset / 256;
        x -= START_OFFSET;
        if (x < 0 || x >= 240) return;
        bm.set(williams_bitmap.line[x], ((y * 2) + ((flag >> 5) & 1)));
        pb1 = inverse_colors[bm.read(0)];
        pb2 = inverse_colors[bm.read(1)];
        if ((flag & 0x08) == 0) {
            if ((flag & 0x10) == 0) {
                if ((flag & 0x80) == 0) pb1 = p1;
                if ((flag & 0x40) == 0) pb2 = p2;
            } else {
                if ((flag & 0x80) == 0) pb1 = RAM[0xCA01] >> 4;
                if ((flag & 0x40) == 0) pb2 = RAM[0xCA01] & 0x0F;
            }
        } else {
            if ((flag & 0x10) == 0) {
                if (p1 != 0) if ((flag & 0x80) == 0) pb1 = p1;
                if (p2 != 0) if ((flag & 0x40) == 0) pb2 = p2;
            } else {
                if (p1 != 0) if ((flag & 0x80) == 0) pb1 = RAM[0xCA01] >> 4;
                if (p2 != 0) if ((flag & 0x40) == 0) pb2 = RAM[0xCA01] & 0x0f;
            }
        }
        if (offset >= 0x9800) {
            if (offset >= 0xE000) return;
            RAM[offset] = (char) data;
            return;
        }
        vram[offset] = (char) ((pb1 << 4) + pb2);
        bm.write(0, Machine.gfx[0].colortable.read(pb1));
        bm.write(1, Machine.gfx[0].colortable.read(pb2));
    }
