    private ColumnBlock searchChildren(int line, int startIndex, int stopIndex) {
        if (children != null) {
            if (startIndex > stopIndex) {
                return (ColumnBlock) children.get(startIndex);
            }
            int currentSearchIndex = (startIndex + stopIndex) / 2;
            int found = ((ColumnBlock) children.get(currentSearchIndex)).isLineWithinThisBlock(line);
            if (found == 0) {
                return (ColumnBlock) children.get(currentSearchIndex);
            } else if (found > 0) {
                if (children.size() - 1 > currentSearchIndex) {
                    return searchChildren(line, currentSearchIndex + 1, stopIndex);
                } else {
                    return null;
                }
            } else if (found < 0) {
                if (currentSearchIndex > 0) {
                    return searchChildren(line, startIndex, currentSearchIndex - 1);
                } else {
                    return (ColumnBlock) children.get(0);
                }
            }
        }
        return null;
    }
