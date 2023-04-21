    public int writeTrace(float[] trace) throws SeisException {
        if (!_trace_array_initialized) {
            throw new SeisException("Trace array not initialized");
        }
        if (!_read_write) {
            throw new SeisException("Frame not writeable");
        }
        if (trace == null) return 0;
        int retval = Math.min(_trace_data_array[_trace_index].length, trace.length);
        for (int k2 = 0; k2 < retval; k2++) {
            _trace_data_array[_trace_index][k2] = trace[k2];
        }
        _trace_array_modified = true;
        return retval;
    }
