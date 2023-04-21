    private void sort(int l, int r) {
        int i, j;
        if ((r - l) > 4) {
            i = (r + l) / 2;
            switch(sort_criteria) {
                case SORT_BYNAME:
                    String s1 = chans[i].getTag();
                    String s2 = chans[l].getTag();
                    String s3 = chans[r].getTag();
                    if (collator.compare(s2, s1) < 0) swap(l, i);
                    if (collator.compare(s2, s3) < 0) swap(l, r);
                    if (collator.compare(s1, s3) < 0) swap(i, r);
                    break;
                case SORT_BYNUM:
                    int comp1 = chans[i].getUsers();
                    int comp2 = chans[l].getUsers();
                    int comp3 = chans[r].getUsers();
                    if (comp2 < comp1) swap(l, i);
                    if (comp2 < comp3) swap(l, r);
                    if (comp1 < comp3) swap(i, r);
            }
            j = r - 1;
            swap(i, j);
            i = l;
            switch(sort_criteria) {
                case SORT_BYNAME:
                    String v = chans[j].getTag();
                    for (; ; ) {
                        while (collator.compare(chans[++i].getTag(), v) > 0) ;
                        while (collator.compare(chans[--j].getTag(), v) < 0) ;
                        if (j < i) break;
                        swap(i, j);
                    }
                    break;
                case SORT_BYNUM:
                    int w = chans[j].getUsers();
                    for (; ; ) {
                        while (chans[++i].getUsers() > w) ;
                        while (chans[--j].getUsers() < w) ;
                        if (j < i) break;
                        swap(i, j);
                    }
            }
            swap(i, r - 1);
            sort(l, j);
            sort(i + 1, r);
        }
    }
