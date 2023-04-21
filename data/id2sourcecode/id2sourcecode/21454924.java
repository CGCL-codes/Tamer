    private static void merge(List<Term> terms, int s, int l) {
        ArrayList<Term> temp = new ArrayList();
        int m = (l + s) / 2;
        int s1 = s;
        int u = m + 1;
        while (s1 <= m && u <= l) {
            if (terms.get(s1).compareTo(terms.get(u)) > 0) {
                temp.add(terms.get(s1));
                s1++;
            } else {
                temp.add(terms.get(u));
                u++;
            }
        }
        while (s1 <= m) {
            temp.add(terms.get(s1));
            s1++;
        }
        while (u <= l) {
            temp.add(terms.get(u));
            u++;
        }
        for (int i = s; i <= l; i++) {
            terms.set(i, temp.get(i - s));
        }
    }
