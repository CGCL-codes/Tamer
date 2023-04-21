    static void decompressXOR(BitBuffer src, Buffer dest, int m) {
        int mask = (1 << m) - 1;
        int last = 0;
        while (true) {
            int tag = src.readBits(1);
            if (tag < 0) return;
            if (tag != 0) {
                dest.writeByte((byte) (last = (last & ~mask) | src.readBits(m)));
            } else {
                last = src.readOffstreamByte();
                if (last < 0) return;
                dest.writeByte((byte) last);
            }
        }
    }
