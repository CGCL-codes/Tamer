    private static void sortBlock(File[] files, int start, int end, File[] mergeTemp) {
        final int length = end - start + 1;
        if (length < 8) {
            for (int i = end; i > start; --i) {
                for (int j = end; j > start; --j) {
                    if (compareFiles(files[j - 1], files[j]) > 0) {
                        final File temp = files[j];
                        files[j] = files[j - 1];
                        files[j - 1] = temp;
                    }
                }
            }
            return;
        }
        final int mid = (start + end) / 2;
        sortBlock(files, start, mid, mergeTemp);
        sortBlock(files, mid + 1, end, mergeTemp);
        int x = start;
        int y = mid + 1;
        for (int i = 0; i < length; ++i) {
            if ((x > mid) || ((y <= end) && compareFiles(files[x], files[y]) > 0)) {
                mergeTemp[i] = files[y++];
            } else {
                mergeTemp[i] = files[x++];
            }
        }
        for (int i = 0; i < length; ++i) files[i + start] = mergeTemp[i];
    }
