    void mergesort(Vector a, Vector b, int l, int r, XPathContext support) throws TransformerException {
        if ((r - l) > 0) {
            int m = (r + l) / 2;
            mergesort(a, b, l, m, support);
            mergesort(a, b, m + 1, r, support);
            int i, j, k;
            for (i = m; i >= l; i--) {
                if (i >= b.size()) b.insertElementAt(a.elementAt(i), i); else b.setElementAt(a.elementAt(i), i);
            }
            i = l;
            for (j = (m + 1); j <= r; j++) {
                if (r + m + 1 - j >= b.size()) b.insertElementAt(a.elementAt(j), r + m + 1 - j); else b.setElementAt(a.elementAt(j), r + m + 1 - j);
            }
            j = r;
            int compVal;
            for (k = l; k <= r; k++) {
                if (i == j) compVal = -1; else compVal = compare((NodeCompareElem) b.elementAt(i), (NodeCompareElem) b.elementAt(j), 0, support);
                if (compVal < 0) {
                    a.setElementAt(b.elementAt(i), k);
                    i++;
                } else if (compVal > 0) {
                    a.setElementAt(b.elementAt(j), k);
                    j--;
                }
            }
        }
    }
