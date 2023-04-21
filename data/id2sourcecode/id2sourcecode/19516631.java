    private IndexSubTable binarySearchIndexSubTables(int glyphId) {
        List<IndexSubTable> subTableList = getIndexSubTableList();
        int index = 0;
        int bottom = 0;
        int top = subTableList.size();
        while (top != bottom) {
            index = (top + bottom) / 2;
            IndexSubTable subTable = subTableList.get(index);
            if (glyphId < subTable.firstGlyphIndex()) {
                top = index;
            } else {
                if (glyphId <= subTable.lastGlyphIndex()) {
                    return subTable;
                }
                bottom = index + 1;
            }
        }
        return null;
    }
