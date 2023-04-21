    protected int indexForElement(Widget parent, Object element) {
        ViewerSorter sorter = getSorter();
        Item[] items = getChildren(parent);
        int count = items.length;
        if (sorter == null) return count;
        int min = 0, max = count - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            Object data = items[mid].getData();
            int compare = sorter.compare(this, data, element);
            if (compare == 0) {
                while (compare == 0) {
                    ++mid;
                    if (mid >= count) {
                        break;
                    }
                    data = items[mid].getData();
                    compare = sorter.compare(this, data, element);
                }
                return mid;
            }
            if (compare < 0) min = mid + 1; else max = mid - 1;
        }
        return min;
    }
