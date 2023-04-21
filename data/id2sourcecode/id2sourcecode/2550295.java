    private static void inplace_merge(int first, int middle, int last, IntComparator comp, Swapper swapper) {
        if (first >= middle || middle >= last) return;
        if (last - first == 2) {
            if (comp.compare(middle, first) < 0) {
                swapper.swap(first, middle);
            }
            return;
        }
        int firstCut;
        int secondCut;
        if (middle - first > last - middle) {
            firstCut = first + (middle - first) / 2;
            secondCut = lower_bound(middle, last, firstCut, comp);
        } else {
            secondCut = middle + (last - middle) / 2;
            firstCut = upper_bound(first, middle, secondCut, comp);
        }
        int first2 = firstCut;
        int middle2 = middle;
        int last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            int first1 = first2;
            int last1 = middle2;
            while (first1 < --last1) swapper.swap(first1++, last1);
            first1 = middle2;
            last1 = last2;
            while (first1 < --last1) swapper.swap(first1++, last1);
            first1 = first2;
            last1 = last2;
            while (first1 < --last1) swapper.swap(first1++, last1);
        }
        middle = firstCut + (secondCut - middle);
        inplace_merge(first, firstCut, middle, comp, swapper);
        inplace_merge(middle, secondCut, last, comp, swapper);
    }
