    public final String nextNameLevel(int nameOffsetI) {
        String nextLevelR = null;
        if (nameOffsetI < getChannelName().length()) {
            int endIndex = getChannelName().indexOf("/", nameOffsetI + 1);
            if (endIndex == -1) {
                endIndex = getChannelName().length();
            }
            nextLevelR = getChannelName().substring(nameOffsetI + 1, endIndex);
        }
        return (nextLevelR);
    }
