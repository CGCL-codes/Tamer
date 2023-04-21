    @Override
    public int normalizeTo(byte[] bs, int begin) {
        writeU2(bs, begin, readU2(bytes, beginBi));
        writeS4(bs, begin + 2, normalizeByteN() - 6);
        writeU2(bs, begin + 6, lineN);
        if (beginAds == null) {
            System.arraycopy(bytes, beginBi + 8, bs, begin + 8, lineN << 2);
            return begin + normalizeByteN();
        }
        begin += 8;
        for (int i = 0; i < lineN; i++, begin += 4) {
            writeU2(bs, begin, beginAds[i]);
            writeU2(bs, begin + 2, lines[i]);
        }
        return begin;
    }
