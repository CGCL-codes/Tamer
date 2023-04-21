    static void VGA_CopyRow(short cleft, short cright, short rold, short rnew, int base) {
        int src, dest;
        int copy;
        short cheight = Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CHAR_HEIGHT);
        dest = base + 8 * ((Int10_modes.CurMode.twidth * rnew) * cheight + cleft);
        src = base + 8 * ((Int10_modes.CurMode.twidth * rold) * cheight + cleft);
        int nextline = 8 * Int10_modes.CurMode.twidth;
        int rowsize = 8 * (cright - cleft);
        copy = cheight;
        for (; copy > 0; copy--) {
            for (int x = 0; x < rowsize; x++) Memory.mem_writeb(dest + x, Memory.mem_readb(src + x));
            dest += nextline;
            src += nextline;
        }
    }
