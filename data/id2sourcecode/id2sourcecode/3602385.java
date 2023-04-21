    static void decompressHighNibble(BitBuffer src, Buffer dest, int a, int b, int c) {
        while (true) {
            int tag = src.readBits(2);
            switch(tag) {
                case 1:
                    dest.writeByte((byte) ((a << 4) | src.readBits(4)));
                    break;
                case 2:
                    dest.writeByte((byte) ((b << 4) | src.readBits(4)));
                    break;
                case 3:
                    dest.writeByte((byte) ((c << 4) | src.readBits(4)));
                    break;
                default:
                    int s = src.readOffstreamByte();
                    if (s < 0) return;
                    dest.writeByte((byte) s);
            }
        }
    }
