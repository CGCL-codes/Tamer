    private String replace(String source, List replaceStructList) {
        int listSize = replaceStructList.size();
        if (listSize == 0) {
            return source;
        }
        ReplaceStruct which = null;
        int smallestIndex = -1;
        for (int i = listSize - 1; i >= 0; i--) {
            ReplaceStruct replace = (ReplaceStruct) replaceStructList.get(i);
            int index = source.indexOf(replace.from);
            if (index == -1) {
                replaceStructList.remove(i);
            } else {
                if ((smallestIndex == -1) || (index < smallestIndex)) {
                    smallestIndex = index;
                    which = replace;
                }
                replace.index = index;
            }
        }
        if ((listSize = replaceStructList.size()) == 0) {
            return source;
        }
        StringBuffer strBuf = new StringBuffer(source.length());
        strBuf.append(source.substring(0, smallestIndex));
        strBuf.append(which.to);
        int start = smallestIndex + which.from.length();
        while (listSize != 0) {
            smallestIndex = -1;
            which = null;
            for (int i = listSize - 1; i >= 0; i--) {
                ReplaceStruct replace = (ReplaceStruct) replaceStructList.get(i);
                if (replace.index < start) {
                    int index = source.indexOf(replace.from, start);
                    if (index == -1) {
                        replaceStructList.remove(i);
                    } else {
                        if ((smallestIndex == -1) || (index < smallestIndex)) {
                            smallestIndex = index;
                            which = replace;
                        }
                        replace.index = index;
                    }
                } else {
                    int index = replace.index;
                    if ((smallestIndex == -1) || (index < smallestIndex)) {
                        smallestIndex = index;
                        which = replace;
                    }
                }
            }
            if ((listSize = replaceStructList.size()) == 0) {
                strBuf.append(source.substring(start));
                return strBuf.toString();
            }
            strBuf.append(source.substring(start, smallestIndex));
            strBuf.append(which.to);
            start = smallestIndex + which.from.length();
        }
        return null;
    }
