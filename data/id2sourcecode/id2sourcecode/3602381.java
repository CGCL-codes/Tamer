    static void compressDoubleZeros(Buffer src, BitBuffer dest) {
        while (src.remaining() >= 2) {
            byte s = src.readByte(), s2 = src.readByte();
            if (s == 0 && s2 == 0) dest.writeBits(1, 1); else {
                dest.writeBits(0, 1);
                dest.writeOffstreamByte(s);
                dest.writeOffstreamByte(s2);
            }
        }
        if (src.remaining() != 0) {
            dest.writeBits(0, 1);
            dest.writeOffstreamByte(src.readByte());
        }
        dest.close();
    }
