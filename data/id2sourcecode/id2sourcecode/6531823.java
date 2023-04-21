    @Override
    public int normalizeTo(byte[] bs, int begin) {
        if (annos == null) {
            System.arraycopy(bytes, beginBi, bs, begin, byteN0());
            return begin + byteN0();
        }
        writeU2(bs, begin, readU2(bytes, beginBi));
        writeU1(bs, begin + 6, paramN);
        int bi = begin + 7;
        for (int gi = 0; gi < paramN; gi++) {
            bi = writeU2(bs, bi, annoNs[gi]);
            for (int ai = 0; ai < annoNs[gi]; ai++) bi = annos[gi][ai].normalizeTo(bs, bi);
        }
        writeS4(bs, begin + 2, bi - begin - 6);
        return bi;
    }
