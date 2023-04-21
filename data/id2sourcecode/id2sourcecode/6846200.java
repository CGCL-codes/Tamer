    public boolean copyTo(Frame output) {
        boolean retval = true;
        if (_using_headers) {
            output._properties = copyTo(output._properties);
            retval = output._properties != null;
        }
        if (retval) {
            output._trace_data_array = copyTo(output._trace_data_array);
            retval = output._trace_data_array != null;
            if (retval) {
                output._first_trace = _first_trace;
                output._last_trace = _last_trace;
                output._traces_in_frame = _traces_in_frame;
                output._trace_index = _trace_index;
                output._using_headers = _using_headers;
                output._header_accessed = _header_accessed;
                output._header_modified = _header_modified;
                output._headers_initialized = _headers_initialized;
                output._trace_array_accessed = _trace_array_accessed;
                output._trace_array_initialized = _trace_array_initialized;
                output._trace_array_modified = _trace_array_modified;
                output._read_write = _read_write;
            }
        }
        return retval;
    }
