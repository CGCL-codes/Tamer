    private String[] readStringIndex(byte[] content, int start, int offsize, int count) {
        LogWriter.writeMethod("{readStringIndex}", 0);
        top = start + getWord(content, top + count * offsize, offsize);
        int nStrings = getWord(content, top, 2);
        stringOffSize = content[top + 2];
        top += 3;
        stringIdx = top;
        stringStart = top + (nStrings + 1) * stringOffSize - 1;
        top = stringStart + getWord(content, top + nStrings * stringOffSize, stringOffSize);
        int[] offsets = new int[nStrings + 2];
        String[] strings = new String[nStrings + 2];
        int ii = stringIdx;
        for (int jj = 0; jj < nStrings + 1; jj++) {
            offsets[jj] = getWord(content, ii, stringOffSize);
            ii = ii + stringOffSize;
        }
        offsets[nStrings + 1] = top - stringStart;
        int current = 0;
        for (int jj = 0; jj < nStrings + 1; jj++) {
            StringBuffer nextString = new StringBuffer();
            for (int c = current; c < offsets[jj]; c++) nextString.append((char) content[stringStart + c]);
            if (debugFont) System.out.println("String " + jj + " =" + nextString);
            strings[jj] = nextString.toString();
            current = offsets[jj];
        }
        return strings;
    }
