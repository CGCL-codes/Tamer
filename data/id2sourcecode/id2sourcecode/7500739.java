    private static final void merge(Object[] _src, Object[] _dst, int _low, int _high, FuComparator _cmp) {
        int length = _high - _low;
        if (length < 7) {
            for (int i = _low; i < _high; i++) for (int j = i; (j > _low) && (_cmp.compare(_dst[j - 1], _dst[j]) > 0); j--) swap(_dst, j, j - 1);
            return;
        }
        int mid = (_low + _high) / 2;
        merge(_dst, _src, _low, mid, _cmp);
        merge(_dst, _src, mid, _high, _cmp);
        if (_cmp.compare(_src[mid - 1], _src[mid]) <= 0) {
            System.arraycopy(_src, _low, _dst, _low, length);
            return;
        }
        int p = _low;
        int q = mid;
        for (int i = _low; i < _high; i++) {
            if ((q >= _high) || (p < mid) && (_cmp.compare(_src[p], _src[q]) <= 0)) _dst[i] = _src[p++]; else _dst[i] = _src[q++];
        }
    }
