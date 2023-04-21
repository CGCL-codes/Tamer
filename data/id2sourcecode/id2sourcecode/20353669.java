    private static TDTree buildTDTree(int first, int last, TwoOrderObject[] elements, int comparison) {
        if (first > last) {
            return null;
        } else if (first == last) {
            return new TDTree(elements[first], comparison);
        } else {
            int half = (first + last) / 2;
            TDTree t = new TDTree(elements[half], comparison);
            t.left = buildTDTree(first, half - 1, elements, 1 - comparison);
            if (t.left != null) t.left.father = t;
            t.right = buildTDTree(half + 1, last, elements, 1 - comparison);
            if (t.right != null) t.right.father = t;
            return t;
        }
    }
