    @Override
    public int normalizeTo(byte[] bs, int begin) {
        writeU2(bs, begin, readU2(bytes, beginBi));
        writeS4(bs, begin + 2, normalizeByteN() - 6);
        writeU2(bs, begin + 6, varN);
        if (beginAds == null) {
            System.arraycopy(bytes, beginBi + 8, bs, begin + 8, varN * 10);
            return begin + normalizeByteN();
        }
        begin += 8;
        for (int i = 0; i < varN; i++, begin += 10) {
            writeU2(bs, begin, beginAds[i]);
            writeU2(bs, begin + 2, adNs[i]);
            writeU2(bs, begin + 4, nameCis[i]);
            writeU2(bs, begin + 6, descCis[i]);
            writeU2(bs, begin + 8, locals[i]);
        }
        return begin;
    }
