    public int pack(Buffer buffer) {
        buffer.write(0x01, 8);
        buffer.write("vorbis");
        buffer.write(0x00, 32);
        buffer.write(getChannels(), 8);
        buffer.write(getRate(), 32);
        buffer.write(getBitrateUpper(), 32);
        buffer.write(getBitrateNominal(), 32);
        buffer.write(getBitrateLower(), 32);
        buffer.write(ilog2(getBlocksize0()), 4);
        buffer.write(ilog2(getBlocksize1()), 4);
        buffer.write(1, 1);
        return 0;
    }
