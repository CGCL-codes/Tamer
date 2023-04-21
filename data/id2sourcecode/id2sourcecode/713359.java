    private int binarySearchWord(int low, int high, String word) {
        boolean naik = false;
        boolean turun = false;
        int mid;
        int start, end;
        while (low <= high) {
            mid = (low + high) / 2;
            start = cariAwal(mid);
            end = cariAkhir(mid);
            if (start < low || end > high) return -1;
            int hasil = compare(array1, start, end, word);
            if (hasil == 0) {
                length = end - start + 1;
                return start;
            } else if (hasil < 0) {
                low = end + 3;
                naik = true;
            } else {
                high = start - 3;
                turun = true;
            }
        }
        if (naik && turun) {
            return -2;
        } else return -1;
    }
