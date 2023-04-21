    private void merge(Vector v, Vector temp, int s, int l) {
        temp.clear();
        int mid = (l + s) / 2;
        int s1 = s;
        int s2 = mid + 1;
        while (s1 <= mid && s2 <= l) {
            if (((Comparable) v.get(s1)).compareTo((Comparable) v.get(s2)) < 0) {
                temp.add(v.get(s1));
                s1++;
            } else {
                temp.add(v.get(s2));
                s2++;
            }
        }
        while (s1 <= mid) {
            temp.add(v.get(s1));
            s1++;
        }
        while (s2 <= l) {
            temp.add(v.get(s2));
            s2++;
        }
    }
