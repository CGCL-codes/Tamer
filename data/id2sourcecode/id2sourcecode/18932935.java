    private static int[] count(final byte[] query, final int begin, final int end, BWTIndex referenceIndex) {
        int start1, end1;
        int start2, end2;
        int left, middle, right;
        start1 = 0;
        end1 = referenceIndex.size();
        for (int i = end - 1; i >= begin; i--) {
            start2 = referenceIndex.getFirstIndexPosition(query[i]);
            end2 = referenceIndex.getFirstIndexPosition((byte) (query[i] + 1)) - 1;
            left = start2;
            right = end2;
            middle = -1;
            while (left <= right) {
                middle = (left + right) / 2;
                if (referenceIndex.getSuccedingIndexPosition(middle) < start1) {
                    left = middle + 1;
                } else if (referenceIndex.getSuccedingIndexPosition(middle) > end1) {
                    right = middle - 1;
                } else {
                    break;
                }
            }
            final int middle2 = middle;
            start2 = left;
            end2 = right;
            if (start2 > end2) {
                start1 = start2;
                end1 = end2;
                break;
            }
            assert (left == start2);
            right = middle2;
            while (left <= right) {
                middle = (left + right) / 2;
                if (referenceIndex.getSuccedingIndexPosition(middle) < start1) {
                    left = middle + 1;
                } else if (referenceIndex.getSuccedingIndexPosition(middle) > start1) {
                    right = middle - 1;
                } else {
                    left = middle;
                    break;
                }
            }
            start2 = left;
            left = middle2;
            right = end2;
            while (left <= right) {
                middle = (left + right) / 2;
                if (referenceIndex.getSuccedingIndexPosition(middle) < end1) {
                    left = middle + 1;
                } else if (referenceIndex.getSuccedingIndexPosition(middle) > end1) {
                    right = middle - 1;
                } else {
                    right = middle;
                    break;
                }
            }
            end2 = right;
            if (start2 > end2) {
                start1 = start2;
                end1 = end2;
                break;
            }
            start1 = start2;
            end1 = end2;
        }
        return new int[] { start1, end1 + 1 };
    }
