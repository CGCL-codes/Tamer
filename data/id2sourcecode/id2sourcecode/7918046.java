    static int findInCharMap(int ch) {
        if (charMap == null || charMap.length == 0) {
            return -1;
        }
        int top, bottom, current;
        bottom = 0;
        top = charMap.length;
        current = top / 2;
        while (top - bottom > 1) {
            if (ch >= charMap[current][0][0]) {
                bottom = current;
            } else {
                top = current;
            }
            current = (top + bottom) / 2;
        }
        if (ch == charMap[current][0][0]) return current; else return -1;
    }
