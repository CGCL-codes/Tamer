    private static void mem_strcpy(int dest, int src) {
        short r;
        while ((r = mem_readb(src++)) != 0) Paging.mem_writeb_inline(dest++, r);
        Paging.mem_writeb_inline(dest, (short) 0);
    }
