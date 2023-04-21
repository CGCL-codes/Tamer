    public boolean copyFrom(Frame input) {
        boolean retval = copyFrom(input._properties);
        if (retval) retval = copyFrom(input._trace_data_array);
        if (retval) {
            _first_trace = input._first_trace;
            _last_trace = input._last_trace;
            _traces_in_frame = input._traces_in_frame;
            _trace_index = input._trace_index;
            _using_headers = input._using_headers;
            _header_accessed = input._header_accessed;
            _header_modified = input._header_modified;
            _headers_initialized = input._headers_initialized;
            _trace_array_accessed = input._trace_array_accessed;
            _trace_array_initialized = input._trace_array_initialized;
            _trace_array_modified = input._trace_array_modified;
            _read_write = input._read_write;
        }
        return retval;
    }
