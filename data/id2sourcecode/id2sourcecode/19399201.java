    private void sortDataMessage(int start, int end) {
        Message pivot, temp;
        int lo = start;
        int hi = end;
        if (lo >= hi) return; else if (lo == hi - 1) {
            if (messageArr[lo].getDate().compareTo(messageArr[hi].getDate()) > 0) {
                pivot = messageArr[lo];
                messageArr[lo] = messageArr[hi];
                messageArr[hi] = pivot;
            }
            return;
        }
        tempInt = (lo + hi) / 2;
        pivot = messageArr[tempInt];
        messageArr[tempInt] = messageArr[hi];
        messageArr[hi] = pivot;
        while (lo < hi) {
            while ((messageArr[lo].getDate().compareTo(pivot.getDate()) <= 0) && (lo < hi)) lo++;
            while ((pivot.getDate().compareTo(messageArr[hi].getDate()) <= 0) && (lo < hi)) hi--;
            if (lo < hi) {
                temp = messageArr[lo];
                messageArr[lo] = messageArr[hi];
                messageArr[hi] = temp;
            }
        }
        messageArr[end] = messageArr[hi];
        messageArr[hi] = pivot;
        sortDataMessage(start, lo - 1);
        sortDataMessage(hi + 1, end);
    }
