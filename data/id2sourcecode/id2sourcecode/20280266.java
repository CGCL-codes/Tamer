    private void lookupGlobNodeSuffix(String fileName, int numEntries, int offset, boolean ignoreCase, int len, Collection mimeTypes, StringBuffer pattern) {
        char character = ignoreCase ? fileName.toLowerCase().charAt(len - 1) : fileName.charAt(len - 1);
        if (character == 0) {
            return;
        }
        int min = 0;
        int max = numEntries - 1;
        while (max >= min && len >= 0) {
            int mid = (min + max) / 2;
            char matchChar = (char) content.getInt(offset + (12 * mid));
            if (matchChar < character) {
                min = mid + 1;
            } else if (matchChar > character) {
                max = mid - 1;
            } else {
                len--;
                int numChildren = content.getInt(offset + (12 * mid) + 4);
                int childOffset = content.getInt(offset + (12 * mid) + 8);
                if (len > 0) {
                    pattern.append(matchChar);
                    lookupGlobNodeSuffix(fileName, numChildren, childOffset, ignoreCase, len, mimeTypes, pattern);
                }
                if (mimeTypes.isEmpty()) {
                    for (int i = 0; i < numChildren; i++) {
                        matchChar = (char) content.getInt(childOffset + (12 * i));
                        if (matchChar != 0) {
                            break;
                        }
                        int mimeOffset = content.getInt(childOffset + (12 * i) + 4);
                        int weight = content.getInt(childOffset + (12 * i) + 8);
                        mimeTypes.add(new WeightedMimeType(getMimeType(mimeOffset), pattern.toString(), weight));
                    }
                }
                return;
            }
        }
    }
