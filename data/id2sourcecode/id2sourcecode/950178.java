    int findmin(int a[], int l, int r) {
        int mid, min1, min2;
        if (l < r) {
            mid = (l + r) / 2;
            min1 = findmin(a, l, mid);
            min2 = findmin(a, mid + 1, r);
            if (min1 < min2) return min1; else return min2;
        } else return a[l];
    }
