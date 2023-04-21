        private IndexSubTable.Builder<? extends IndexSubTable> binarySearchIndexSubTables(int glyphId) {
            List<IndexSubTable.Builder<? extends IndexSubTable>> subTableList = getIndexSubTableBuilders();
            int index = 0;
            int bottom = 0;
            int top = subTableList.size();
            while (top != bottom) {
                index = (top + bottom) / 2;
                IndexSubTable.Builder<? extends IndexSubTable> subTable = subTableList.get(index);
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
