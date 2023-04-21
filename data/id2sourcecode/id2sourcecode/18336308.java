    private final void readGlobalSubRoutines(byte[] content) throws Exception {
        LogWriter.writeMethod("{readGlobalSubRoutines}", 0);
        int subOffSize = (content[top + 2] & 0xff);
        int count = getWord(content, top, 2);
        top += 3;
        if (count > 0) {
            int idx = top;
            int start = top + (count + 1) * subOffSize - 1;
            top = start + getWord(content, top + count * subOffSize, subOffSize);
            int[] offset = new int[count + 2];
            int ii = idx;
            for (int jj = 0; jj < count + 1; jj++) {
                offset[jj] = start + getWord(content, ii, subOffSize);
                ii = ii + subOffSize;
            }
            offset[count + 1] = top;
            glyphs.setGlobalBias(calculateSubroutineBias(count));
            int current = offset[0];
            for (int jj = 1; jj < count + 1; jj++) {
                ByteArrayOutputStream nextStream = new ByteArrayOutputStream();
                for (int c = current; c < offset[jj]; c++) nextStream.write(content[c]);
                nextStream.close();
                glyphs.setCharString("global" + (jj - 1), nextStream.toByteArray());
                current = offset[jj];
            }
        }
    }
