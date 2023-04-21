    public static void mem_memcpy(int dest, int src, int size) {
        while (size-- != 0) Paging.mem_writeb_inline(dest++, Paging.mem_readb_inline(src++));
    }
