    private static <T> int indexOfLastMatching(List<T> list, Comparator<? super T> comparator, int startIdx, int maxIdx) {
        T value = list.get(startIdx);
        int addMatchIdx = 1;
        int lowerBoundMatchIdx = startIdx;
        int upperBoundMatchIdx = startIdx + addMatchIdx;
        while (true) {
            if (upperBoundMatchIdx >= maxIdx) {
                upperBoundMatchIdx = maxIdx;
                break;
            }
            if (comparator.compare(value, list.get(upperBoundMatchIdx)) == 0) {
                lowerBoundMatchIdx = upperBoundMatchIdx;
                addMatchIdx *= 2;
                upperBoundMatchIdx += addMatchIdx;
            } else {
                break;
            }
        }
        while (true) {
            int midMatchIdx = (lowerBoundMatchIdx + upperBoundMatchIdx) / 2;
            if (lowerBoundMatchIdx == upperBoundMatchIdx) break;
            boolean downToTwo = (lowerBoundMatchIdx == upperBoundMatchIdx - 1);
            if (comparator.compare(value, list.get(midMatchIdx)) == 0) {
                if (downToTwo) {
                    if (comparator.compare(value, list.get(upperBoundMatchIdx)) == 0) {
                        lowerBoundMatchIdx = upperBoundMatchIdx;
                    }
                    break;
                }
                lowerBoundMatchIdx = midMatchIdx;
            } else {
                upperBoundMatchIdx = midMatchIdx - 1;
            }
        }
        return lowerBoundMatchIdx;
    }
