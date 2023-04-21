    public void mutate(Permutation tomutate, int sl) {
        if (len == 2) tomutate.swap(0, 1);
        int from = rand.nextInt(len);
        int subsegmentlength = -1;
        if (sl == -1) subsegmentlength = 2 + rand.nextInt(len - 2); else {
            if (sl < 2) subsegmentlength = 2; else if (sl >= len) subsegmentlength = sl - 1; else subsegmentlength = sl;
        }
        int to = (from + subsegmentlength) % len;
        int elems;
        if (from < to) elems = to - from + 1; else elems = to + len - from + 1;
        int cut = (from + 1 + rand.nextInt(elems - 2)) % len;
        tomutate.invert(from, cut);
        tomutate.invert((cut + 1) % len, to);
    }
