    @SuppressWarnings("unchecked")
    protected int indexForElement(Object element) {
        ViewerComparator comparator = getComparator();
        if (comparator == null) {
            return doGetItemCount();
        }
        int count = doGetItemCount();
        int min = 0, max = count - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            Object data = doGetItem(mid).getData();
            int compare = comparator.compare(this.getPublicViewer(), data, element);
            if (compare == 0) {
                while (compare == 0) {
                    ++mid;
                    if (mid >= count) {
                        break;
                    }
                    data = doGetItem(mid).getData();
                    compare = comparator.compare(this.getPublicViewer(), data, element);
                }
                return mid;
            }
            if (compare < 0) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }
        return min;
    }
