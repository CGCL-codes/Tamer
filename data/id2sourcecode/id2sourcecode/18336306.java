    private final void readGlyphs(byte[] content, int nGlyphs, int[] names) throws Exception {
        LogWriter.writeMethod("{readGlyphs}" + nGlyphs, 0);
        try {
            int glyphOffSize = content[top + 2];
            top += 3;
            int glyphIdx = top;
            int glyphStart = top + (nGlyphs + 1) * glyphOffSize - 1;
            top = glyphStart + getWord(content, top + nGlyphs * glyphOffSize, glyphOffSize);
            int[] glyphoffset = new int[nGlyphs + 2];
            int ii = glyphIdx;
            for (int jj = 0; jj < nGlyphs + 1; jj++) {
                glyphoffset[jj] = glyphStart + getWord(content, ii, glyphOffSize);
                ii = ii + glyphOffSize;
            }
            glyphoffset[nGlyphs + 1] = top;
            int current = glyphoffset[0];
            for (int jj = 1; jj < nGlyphs + 1; jj++) {
                ByteArrayOutputStream nextGlyph = new ByteArrayOutputStream();
                for (int c = current; c < glyphoffset[jj]; c++) nextGlyph.write(content[c]);
                nextGlyph.close();
                if ((isCID)) {
                    glyphs.setCharString("" + names[jj - 1], nextGlyph.toByteArray());
                    if (debugFont) System.out.println("CIDglyph= " + names[jj - 1] + " start=" + current + " length=" + glyphoffset[jj]);
                } else {
                    String name = getString(content, names[jj - 1], stringIdx, stringStart, stringOffSize);
                    glyphs.setCharString(name, nextGlyph.toByteArray());
                    if (debugFont) System.out.println("glyph= " + name + " start=" + current + " length=" + glyphoffset[jj]);
                }
                current = glyphoffset[jj];
            }
        } catch (Exception e) {
        }
    }
