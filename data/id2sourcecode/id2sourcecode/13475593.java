    public void flip(int start, int end) {
        int mid = (start + end) / 2;
        --end;
        for (; start < mid; ++start, --end) {
            int temp = indexChars[start];
            indexChars[start] = indexChars[end];
            indexChars[end] = temp;
        }
    }
