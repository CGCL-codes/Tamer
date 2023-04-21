    public int find(int p) {
        int firstIndex = 0, lastIndex, mid, midpos;
        if (!scanning) {
            moveGap(gap + tokens.length - endgap);
        }
        lastIndex = gap - 1;
        if (p > tokens[lastIndex].position) {
            return lastIndex;
        }
        while (lastIndex > firstIndex + 1) {
            mid = (firstIndex + lastIndex) / 2;
            midpos = tokens[mid].position;
            if (p > midpos) {
                firstIndex = mid;
            } else {
                lastIndex = mid;
            }
        }
        return firstIndex;
    }
