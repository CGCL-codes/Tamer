    private void quickSort(double[] feld, int links, int rechts) {
        if (links >= rechts) return;
        int q = (links + rechts) / 2;
        int l = links, r = rechts, h;
        do {
            while (l < q && feld[l] <= feld[q]) l++;
            while (r > q && feld[q] <= feld[r]) r--;
            if (l == r) break;
            swap(feld, l, r);
            if (r == q) q = l; else if (l == q) q = r;
        } while (true);
        quickSort(feld, links, q - 1);
        quickSort(feld, q + 1, rechts);
    }
