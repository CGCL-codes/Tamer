    private final void readType1CFontFile(byte[] content) throws Exception {
        LogWriter.writeMethod("{readType1CFontFile}", 0);
        LogWriter.writeLog("Embedded Type1C font used");
        glyphs.setis1C(true);
        if (debugFont) System.out.println(getBaseFontName());
        int start;
        int size = 2;
        int major = content[0];
        int minor = content[1];
        if ((major != 1) | (minor != 0)) LogWriter.writeLog("1C  format " + major + ":" + minor + " not fully supported");
        if (debugFont) System.out.println("major=" + major + " minor=" + minor);
        top = content[2];
        int count = getWord(content, top, size);
        int offsize = content[top + size];
        top += (size + 1);
        start = top + (count + 1) * offsize - 1;
        top = start + getWord(content, top + count * offsize, offsize);
        count = getWord(content, top, size);
        offsize = content[top + size];
        top += (size + 1);
        start = top + (count + 1) * offsize - 1;
        int dicStart = start + getWord(content, top, offsize);
        int dicEnd = start + getWord(content, top + offsize, offsize);
        String[] strings = readStringIndex(content, start, offsize, count);
        readGlobalSubRoutines(content);
        decodeDictionary(content, dicStart, dicEnd, strings);
        if (FDSelect != -1) {
            try {
                if (debugDictionary) System.out.println("=============FDSelect====================" + getBaseFontName());
                int nextDic = FDArray;
                count = getWord(content, nextDic, size);
                offsize = content[nextDic + size];
                nextDic += (size + 1);
                start = nextDic + (count + 1) * offsize - 1;
                dicStart = start + getWord(content, nextDic, offsize);
                dicEnd = start + getWord(content, nextDic + offsize, offsize);
                decodeDictionary(content, dicStart, dicEnd, strings);
                if (debugDictionary) System.out.println("=================================" + getBaseFontName());
            } catch (Exception ee) {
                ee.printStackTrace();
                System.exit(1);
            }
        }
        top = charstrings;
        int nGlyphs = getWord(content, top, size);
        if (debugFont) System.out.println("nGlyphs=" + nGlyphs);
        int[] names = readCharset(charset, nGlyphs, charstrings, content);
        if (debugFont) {
            System.out.println("=======charset===============");
            int count2 = names.length;
            for (int jj = 0; jj < count2; jj++) {
                System.out.println(jj + " " + names[jj]);
            }
            System.out.println("=======Encoding===============");
        }
        setEncoding(content, nGlyphs, names);
        top = charstrings;
        readGlyphs(content, nGlyphs, names);
        if (privateDict != -1) {
            try {
                top = privateDict + privateDictOffset;
                if (top + 2 < content.length) {
                    int nSubrs = getWord(content, top, size);
                    if (nSubrs > 0) readSubrs(content, nSubrs);
                } else if (debugFont || debugDictionary) {
                    System.out.println("Private subroutine out of range");
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        isFontEmbedded = true;
    }
