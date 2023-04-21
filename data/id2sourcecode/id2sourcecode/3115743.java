    public static short[] quicksort(short menge[]) {
        if (menge == null) {
            return menge;
        }
        int stapel[] = new int[32];
        int s = 0;
        int links = 0;
        int rechts = menge.length - 1;
        int i;
        int j;
        int a = 0;
        while (a != 1) {
            if (links < rechts) {
                short pivot = menge[rechts - 1];
                i = links - 1;
                j = rechts + 1;
                int b = 0;
                while (b != 1) {
                    do {
                        i++;
                    } while (menge[i] < pivot);
                    do {
                        j--;
                    } while (menge[j] > pivot);
                    if (i >= j) {
                        break;
                    } else {
                        short tmp = menge[i];
                        menge[i] = menge[j];
                        menge[j] = tmp;
                    }
                }
                stapel[s] = rechts;
                s++;
                rechts = wgroesser(links, i - 1);
            } else {
                if (s == 0) {
                    break;
                } else {
                    links = rechts + 1;
                    s--;
                    rechts = stapel[s];
                }
            }
        }
        return menge;
    }
