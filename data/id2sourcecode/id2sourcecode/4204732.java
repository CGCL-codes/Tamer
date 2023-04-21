    @Override
    public int normalizeTo(byte[] bs, int begin) {
        if (annos == null) {
            System.arraycopy(bytes, beginBi, bs, begin, byteN0());
            return begin + byteN0();
        }
        writeU2(bs, begin, readU2(bytes, beginBi));
        writeU2(bs, begin + 6, annoN);
        int bi = begin + 8;
        for (int i = 0; i < annoN; i++) bi = annos[i].normalizeTo(bs, bi);
        writeS4(bs, begin + 2, bi - begin - 6);
        return bi;
    }
