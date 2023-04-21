    public float[][] getArray() {
        if (_read_write && _trace_array_initialized) {
            _trace_array_accessed = true;
        }
        return _trace_data_array;
    }
