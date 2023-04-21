    private static void mergeSort(List<Integer> list, int from, int to, List<Integer> temporary) {
        if (to <= from) {
            return;
        }
        int middle = (from + to) / 2;
        mergeSort(list, from, middle, temporary);
        mergeSort(list, middle + 1, to, temporary);
        merge(list, from, to, middle, temporary);
    }
