    static void EGA16_CopyRow(short cleft, short cright, short rold, short rnew, int base) {
        int src, dest;
        int copy;
        short cheight = Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CHAR_HEIGHT);
        dest = base + (Int10_modes.CurMode.twidth * rnew) * cheight + cleft;
        src = base + (Int10_modes.CurMode.twidth * rold) * cheight + cleft;
        int nextline = Int10_modes.CurMode.twidth;
        IoHandler.IO_Write(0x3ce, 5);
        IoHandler.IO_Write(0x3cf, 1);
        IoHandler.IO_Write(0x3c4, 2);
        IoHandler.IO_Write(0x3c5, 0xf);
        int rowsize = (cright - cleft);
        copy = cheight;
        for (; copy > 0; copy--) {
            for (int x = 0; x < rowsize; x++) Memory.mem_writeb(dest + x, Memory.mem_readb(src + x));
            dest += nextline;
            src += nextline;
        }
        IoHandler.IO_Write(0x3ce, 5);
        IoHandler.IO_Write(0x3cf, 0);
    }
