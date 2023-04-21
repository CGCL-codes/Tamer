    public void remove(int index) {
        for (int i = index; i < _size - 1; i++) {
            _array[i] = _array[i + 1];
        }
        _array[_size - 1] = null;
    }
