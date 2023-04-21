    protected final int getNgramId(final int historyIndexId, final int indexHistoryId, final int typeId) {
        if (historyIds == null || typeBounds == null) return -1;
        if (historyIndexId < 0 || historyIndexId >= historyIds.length) return -1;
        final UnsignedArray subHistoryIds = historyIds[historyIndexId];
        if (subHistoryIds == null) return -1;
        final UnsignedArray subTypeBounds = typeBounds[historyIndexId];
        if (subTypeBounds == null || typeId >= subTypeBounds.size()) return -1;
        int lowerBound = typeId == 0 ? 0 : (int) subTypeBounds.get(typeId - 1);
        int upperBound = (int) subTypeBounds.get(typeId);
        while (lowerBound != upperBound) {
            final int indexNgramId = lowerBound + (upperBound - lowerBound) / 2;
            final long cmp = subHistoryIds.get(indexNgramId) - indexHistoryId;
            if (cmp > 0) upperBound = indexNgramId; else if (cmp < 0) lowerBound = indexNgramId + 1; else return indexNgramId;
        }
        return -1;
    }
