    public static void calculateIndex(String[] array, String text) {
        startIndex = -1;
        endIndex = -2;
        int min = 0;
        int max = array.length - 1;
        int mid = 0;
        while (true) {
            mid = (min + max) / 2;
            if (array[mid].compareTo(text) < 0) min = mid + 1; else max = mid - 1;
            if (array[mid].startsWith(text) || min > max) break;
        }
        if (array[mid].startsWith(text)) {
            startIndex = mid;
            endIndex = mid;
            try {
                while (true) {
                    if (array[startIndex - 1].startsWith(text)) startIndex--; else break;
                }
                while (true) {
                    if (array[endIndex + 1].startsWith(text)) endIndex++; else break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }
