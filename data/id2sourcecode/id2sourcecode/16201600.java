    private void QuickSort(int a[], int lowIndex, int highIndex, RecordComparator comparator) throws RecordStoreException {
        int left = lowIndex;
        int right = highIndex;
        if (highIndex > lowIndex) {
            int ind = (lowIndex + highIndex) / 2;
            int pivotIndex = a[ind];
            byte[] pivotData = recordStore.getRecord(pivotIndex);
            while (left <= right) {
                while ((left < highIndex) && (comparator.compare(recordStore.getRecord(a[left]), pivotData) == RecordComparator.PRECEDES)) {
                    left++;
                }
                while ((right > lowIndex) && (comparator.compare(recordStore.getRecord(a[right]), pivotData) == RecordComparator.FOLLOWS)) {
                    right--;
                }
                if (left <= right) {
                    int tmp = a[left];
                    a[left] = a[right];
                    a[right] = tmp;
                    left++;
                    right--;
                }
            }
            if (lowIndex < right) {
                QuickSort(a, lowIndex, right, comparator);
            }
            if (left < highIndex) {
                QuickSort(a, left, highIndex, comparator);
            }
        }
    }
