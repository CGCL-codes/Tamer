    @Override
    int indexOf(K key) {
        int left = 0;
        int right = _size - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            int i = BPlusTree.compare(_keys[middle], key);
            if (i == 0) {
                left = middle + 1;
                break;
            }
            if (i < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        if (_size <= left) return _size - 1;
        assert 0 <= left;
        return left;
    }
