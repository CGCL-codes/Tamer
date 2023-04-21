    private int searchPattern(String pattern) {
        int from = 0;
        int to = rootItems.length;
        int i;
        int res;
        while (to > from) {
            i = (to + from) / 2;
            res = compare(rootItems[i], pattern);
            if (res == 0) {
                while (i > 0) {
                    if (compare(rootItems[--i], pattern) != 0) {
                        i++;
                        break;
                    }
                }
                return i;
            }
            if (res < 0) {
                from = i + 1;
            } else {
                to = i;
            }
        }
        return -1;
    }
