    public int findSpan(double s) {
        if (s >= _knots.getValue(_nu + 1)) return _nu;
        int low = _degree;
        int high = _nu + 1;
        int mid = (low + high) / 2;
        while ((s < _knots.getValue(mid) || s >= _knots.getValue(mid + 1)) && low < high) {
            if (s < _knots.getValue(mid)) high = mid; else low = mid;
            mid = (low + high) / 2;
        }
        return mid;
    }
