    protected static int[] findRange(int offset, int length, List partitions) {
        int start = 0;
        int end = 0;
        int left = 0;
        int right = partitions.size();
        if (right == 0) {
            return new int[] { -1, -1 };
        } else if (offset == getLength(partitions)) {
            return new int[] { right - 1, right - 1 };
        }
        while (left <= right) {
            int mid = (right + left) / 2;
            Region region = (Region) partitions.get(mid);
            if (isAtStart(offset, region)) {
                start = mid == 0 ? 0 : mid - 1;
                break;
            } else if (isWithin(offset, region)) {
                start = mid;
                break;
            } else if (isAtEnd(offset, region)) {
                start = mid;
                break;
            } else if (isAfter(offset, region)) {
                left = mid + 1;
            } else if (isBefore(offset, region)) {
                right = mid - 1;
            }
        }
        left = start;
        right = partitions.size();
        while (left <= right) {
            int mid = (right + left) / 2;
            Region region = (Region) partitions.get(mid);
            if (isAtStart(offset + length, region)) {
                end = mid;
                break;
            } else if (isWithin(offset + length, region)) {
                end = mid;
                break;
            } else if (isAtEnd(offset + length, region)) {
                end = mid == partitions.size() - 1 ? mid : mid + 1;
                break;
            } else if (isAfter(offset + length, region)) {
                left = mid + 1;
            } else if (isBefore(offset, region)) {
                right = mid - 1;
            }
        }
        return new int[] { start, end };
    }
