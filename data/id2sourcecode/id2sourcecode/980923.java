    private int _divideGo(int[] input, int start, int end) {
        if (start > end) {
            return 0;
        }
        if (start == end) {
            return max(0, input[start]);
        }
        int m = (end + start) / 2;
        return max(toLeftMaxSum(input, start, m) + toRightMaxSum(input, m + 1, end), _divideGo(input, start, m), _divideGo(input, m + 1, end));
    }
