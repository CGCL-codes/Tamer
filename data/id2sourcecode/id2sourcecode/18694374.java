    private Collection<IInterval> gather0(IInterval target) {
        int begin = target.getLeft();
        int end = target.getRight();
        if (begin <= left && right <= end) {
            return intervals();
        } else {
            int mid = (left + right) / 2;
            Collection<IInterval> col1 = new ArrayList<IInterval>();
            Collection<IInterval> col2 = new ArrayList<IInterval>();
            if (begin < mid) {
                col1 = ((StoredIntervalsNode<T>) lson).gather0(target);
            }
            if (mid < end) {
                col2 = ((StoredIntervalsNode<T>) rson).gather0(target);
            }
            for (IInterval iiv : col2) {
                if (!col1.contains(iiv)) {
                    col1.add(iiv);
                }
            }
            for (IInterval iiv : intervals()) {
                if (!col1.contains(iiv)) {
                    col1.add(iiv);
                }
            }
            return col1;
        }
    }
