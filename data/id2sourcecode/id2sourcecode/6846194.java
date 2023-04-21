    public boolean couldHaveChanged() {
        boolean retval = _read_write && _trace_array_initialized && headersValid() && (_trace_array_accessed || _header_accessed || _trace_array_modified || _header_modified);
        return retval;
    }
