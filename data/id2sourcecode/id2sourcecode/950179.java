    int findmax(int a[], int l, int r) {
        int mid, max1, max2;
        if (l < r) {
            mid = (l + r) / 2;
            max1 = findmax(a, l, mid);
            max2 = findmax(a, mid + 1, r);
            if (max1 < max2) return max2; else return max1;
        } else return a[r];
    }
