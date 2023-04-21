    @Override
    public V remove(Object key) {
        V oldValue = get(key);
        if (oldValue != null) {
            int index = Arrays.binarySearch(_keys, 0, _size, key, _comparator);
            for (int i = index; i < _size - 1; i++) {
                _keys[i] = _keys[i + 1];
                _values[i] = _values[i + 1];
            }
            _keys[_size - 1] = _values[_size - 1] = null;
            _size--;
            if (_size < _keys.length - 2 * INCREMENT) resize(_keys.length - INCREMENT);
        }
        return oldValue;
    }
