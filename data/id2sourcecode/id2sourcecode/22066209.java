    private static int[] removeDuplicates(int[] a) {
        int[] tmp = new int[a.length];
        int count = 0;
        tmp[count] = a[count];
        for (int i = 0; i < a.length - 1; i++) {
            if (tmp[count] != a[i + 1]) {
                count++;
                tmp[count] = a[i + 1];
            }
        }
        count++;
        int[] tmp2 = new int[count];
        for (int i = 0; i < tmp2.length; i++) {
            tmp2[i] = tmp[i];
        }
        return tmp2;
    }
