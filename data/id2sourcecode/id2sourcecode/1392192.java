    public TermSlot get(byte[] key) {
        if (slots.size() == 0) {
            return null;
        }
        int big = slots.size() - 1;
        int small = 0;
        while (big - small > 1) {
            int middle = small + (big - small) / 2;
            byte[] middleKey = slots.get(middle).termData;
            if (Utility.compare(middleKey, key) <= 0) {
                small = middle;
            } else {
                big = middle;
            }
        }
        TermSlot one = slots.get(small);
        TermSlot two = slots.get(big);
        if (Utility.compare(two.termData, key) <= 0) {
            return two;
        } else {
            return one;
        }
    }
