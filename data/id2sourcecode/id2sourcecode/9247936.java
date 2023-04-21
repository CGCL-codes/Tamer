    private void sortDataContact(int start, int end) {
        Contact pivot, temp;
        int lo = start;
        int hi = end;
        if (lo >= hi) return; else if (lo == hi - 1) {
            if (contactArr[lo].getName().compareTo(contactArr[hi].getName()) > 0) {
                pivot = contactArr[lo];
                contactArr[lo] = contactArr[hi];
                contactArr[hi] = pivot;
            }
            return;
        }
        tempInt = (lo + hi) / 2;
        pivot = contactArr[tempInt];
        contactArr[tempInt] = contactArr[hi];
        contactArr[hi] = pivot;
        while (lo < hi) {
            while ((contactArr[lo].getName().compareTo(pivot.getName()) <= 0) && (lo < hi)) lo++;
            while ((pivot.getName().compareTo(contactArr[hi].getName()) <= 0) && (lo < hi)) hi--;
            if (lo < hi) {
                temp = contactArr[lo];
                contactArr[lo] = contactArr[hi];
                contactArr[hi] = temp;
            }
        }
        contactArr[end] = contactArr[hi];
        contactArr[hi] = pivot;
        sortDataContact(start, lo - 1);
        sortDataContact(hi + 1, end);
    }
