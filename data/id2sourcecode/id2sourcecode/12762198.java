    private void quickSortByName(FileModel[] list, int left, int right) {
        if (right <= left) {
            return;
        } else if (left == (right - 1)) {
            if (list[left].getName().compareTo(list[right].getName()) > 0) {
                FileModel pivot = list[left];
                list[left] = list[right];
                list[right] = pivot;
                return;
            }
        }
        int center = (right + left) / 2;
        FileModel pivot = list[center];
        int i = left;
        int j = right;
        do {
            while ((list[i].getName().compareTo(pivot.getName()) < 0) && (i < right)) {
                i++;
            }
            while ((list[j].getName().compareTo(pivot.getName()) > 0) && (j > left)) {
                j--;
            }
            if (i <= j) {
                FileModel cache = list[i];
                list[i] = list[j];
                list[j] = cache;
                i++;
                j--;
            }
        } while (i <= j);
        if (j < left) {
            list[center] = list[left];
            list[left] = pivot;
            i++;
            j++;
        }
        quickSortByName(list, left, j);
        quickSortByName(list, i, right);
    }
