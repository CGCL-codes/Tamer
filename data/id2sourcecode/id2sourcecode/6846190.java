    public TraceProperties getHeader() {
        TraceProperties retval = _properties;
        if (!_using_headers) {
            retval = null;
        } else if (_read_write && _headers_initialized) {
            _header_accessed = true;
        }
        return retval;
    }
