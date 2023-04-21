    protected int search(float length) {
        int left = 0;
        int right = result.length;
        int index = (right + left) / 2;
        int maxindex = result.length - 1;
        while (left < right) {
            index = (right + left) / 2;
            if (lengthes[index] == length) return index;
            if (index < maxindex && lengthes[index] < length && lengthes[index + 1] > length) return index;
            if (lengthes[index] > length) {
                right = index;
            } else {
                left = index + 1;
            }
        }
        return maxindex;
    }
