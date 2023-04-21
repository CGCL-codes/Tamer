    public static void MEM_BlockWrite(int pt, byte[] read, int offset, int size) {
        int i;
        for (i = 0; i < size && i < read.length; i++) {
            Paging.mem_writeb_inline(pt++, read[i + offset]);
        }
        for (; i < size; i++) {
            Paging.mem_writeb_inline(pt++, (byte) 0);
        }
    }
