    private void lookupGlobLiteral(String fileName, Collection mimeTypes) {
        int listOffset = getLiteralListOffset();
        int numEntries = content.getInt(listOffset);
        int min = 0;
        int max = numEntries - 1;
        while (max >= min) {
            int mid = (min + max) / 2;
            String literal = getString(content.getInt((listOffset + 4) + (12 * mid)));
            int cmp = literal.compareTo(fileName);
            if (cmp < 0) {
                min = mid + 1;
            } else if (cmp > 0) {
                max = mid - 1;
            } else {
                String mimeType = getMimeType(content.getInt((listOffset + 4) + (12 * mid) + 4));
                int weight = content.getInt((listOffset + 4) + (12 * mid) + 8);
                mimeTypes.add(new WeightedMimeType(mimeType, literal, weight));
                return;
            }
        }
    }
