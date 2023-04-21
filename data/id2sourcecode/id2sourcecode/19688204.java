    private static void quickSort(int left, int right) {
        if (left >= right) return;
        int i = left - 1;
        int j = right;
        int pivotIndex = (i + j) / 2;
        swap(pivotIndex, right);
        String pivotValue = displayNames.get(right).toLowerCase();
        while (true) {
            while (pivotValue.compareTo(displayNames.get(++i).toLowerCase()) > 0) {
                if (i == right) break;
            }
            while (pivotValue.compareTo(displayNames.get(--j).toLowerCase()) < 0) {
                if (j == left) break;
            }
            if (i >= j) break;
            swap(i, j);
        }
        swap(i, right);
        quickSort(left, i - 1);
        quickSort(i + 1, right);
    }
