    public boolean setIndex(long trace_index) throws SeisException {
        boolean retval;
        if (_work_to_do) {
            if (trace_index == _cur_trace) return true;
            retval = updateCurrentTraceAndFrame(trace_index);
            if (retval) {
                if (_cur_trace < _first_trace_in_frame || _cur_trace > _last_trace_in_frame) {
                    if (_read_write) {
                        if (_frame_buffer.isFull()) {
                            retval = writeOldestFrame();
                        }
                    }
                    initFrame();
                    _frame_buffer.setTraceIndex(_cur_trace);
                    if (_read_write) {
                        readCurrentFrame();
                    } else {
                        retval = readCurrentFrame();
                    }
                } else {
                    retval = _frame_buffer.setTraceIndex(_cur_trace);
                }
            }
        } else {
            retval = false;
        }
        return retval;
    }
