    public static void host_memcpy(int dst, int src, int amount) {
        int src_align = src & 0x3;
        int dst_align = dst & 0x3;
        if (src_align == dst_align) {
            while ((src & 0x3) > 0 && amount > 0) {
                host_writeb(dst++, host_readb(src++));
                amount--;
            }
            int len = (amount >>> 2);
            if (len > 0) System.arraycopy(direct, src >>> 2, direct, dst >>> 2, len);
            len = len << 2;
            if (len == amount) return;
            dst += len;
            src += len;
            amount -= len;
        }
        for (int i = 0; i < amount; i++) {
            host_writeb(dst++, host_readb(src++));
        }
    }
