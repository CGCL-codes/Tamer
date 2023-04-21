    public String get(int index) {
        assert index >= 0;
        assert index < documentCount;
        if (index >= documentCount) return "unknown";
        if (index < 0) return "unknown";
        int big = slots.size() - 1;
        int small = 0;
        while (big - small > 1) {
            int middle = small + (big - small) / 2;
            if (slots.get(middle).offset >= index) big = middle; else small = middle;
        }
        NameSlot one = slots.get(small);
        NameSlot two = slots.get(big);
        String result = "";
        if (two.offset <= index) result = getInSlot(two, index); else result = getInSlot(one, index);
        return result;
    }
