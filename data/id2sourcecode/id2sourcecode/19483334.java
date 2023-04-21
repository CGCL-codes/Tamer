    private void merge(int start, int end) {
        int[] temps = new int[end - start + 1];
        int m = (start + end) / 2;
        int i = start;
        int j = m + 1;
        for (int k = 0; k < temps.length; k++) {
            if (array[i] < array[j]) {
                temps[k] = array[i++];
                count += j - m - 1;
            } else {
                temps[k] = array[j++];
            }
            if (i > m || j > end) {
                break;
            }
        }
        for (int k = i + j - m - 1, remainingStart = i > m ? j : i; k < temps.length; k++, remainingStart++) {
            temps[k] = array[remainingStart];
            if (remainingStart <= m) count += j - m - 1;
        }
        for (int k = 0; k < temps.length; k++) {
            array[start++] = temps[k];
        }
    }
