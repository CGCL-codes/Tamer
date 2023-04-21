    public static long binarySearch(char[] selector, MethodBinding[] sortedMethods) {
        if (sortedMethods == null) return -1;
        int max = sortedMethods.length;
        if (max == 0) return -1;
        int left = 0, right = max - 1, selectorLength = selector.length;
        int mid = 0;
        char[] midSelector;
        while (left <= right) {
            mid = left + (right - left) / 2;
            int compare = compare(selector, midSelector = sortedMethods[mid].selector, selectorLength, midSelector.length);
            if (compare < 0) {
                right = mid - 1;
            } else if (compare > 0) {
                left = mid + 1;
            } else {
                int start = mid, end = mid;
                while (start > left && CharOperation.equals(sortedMethods[start - 1].selector, selector)) {
                    start--;
                }
                while (end < right && CharOperation.equals(sortedMethods[end + 1].selector, selector)) {
                    end++;
                }
                return start + ((long) end << 32);
            }
        }
        return -1;
    }
