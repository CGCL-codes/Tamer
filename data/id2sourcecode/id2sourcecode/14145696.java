    @Override
    public int normalizeTo(byte[] bs, int begin) {
        writeU2(bs, begin, readU2(bytes, beginBi));
        writeU4(bs, begin + 2, normalizeByteN() - 6);
        writeU2(bs, begin + 6, exceptionN);
        if (exceptionCis == null) {
            System.arraycopy(bytes, beginBi + 8, bs, begin + 8, exceptionN << 1);
            return begin + normalizeByteN();
        }
        begin += 8;
        for (int i = 0; i < exceptionN; i++, begin += 2) writeU2(bs, begin, exceptionCis[i]);
        return begin;
    }
