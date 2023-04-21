    public TraceAccessor(Seisio seisio, IParallelContext pc, String io_mode, int frame_count) {
        _seisio = seisio;
        _pc = pc;
        if (io_mode.equalsIgnoreCase(Seisio.MODE_READ_WRITE)) {
            _read_write = true;
        } else if (io_mode.equalsIgnoreCase(Seisio.MODE_READ_ONLY)) {
            _read_write = false;
        } else {
            throw new RuntimeException("TraceAccessor: invalid rw-" + io_mode);
        }
        _lengths = _seisio.getGridDefinition().getAxisLengths();
        int k2, size;
        if (_pc != null) {
            for (k2 = _lengths.length - 1; _lengths[k2] < 2 && k2 >= 0; k2--) ;
            k2++;
            if (k2 > 0) {
                size = _pc.size();
            } else {
                size = 1;
            }
            _cur_rank = _pc.rank();
            if (size == 1 && _cur_rank != 0) size = 0;
        } else {
            size = 1;
            _cur_rank = 0;
        }
        if (size != 0) {
            assert (GridDefinition.FRAME_INDEX < _lengths.length);
            _traces_in_frame = (int) _lengths[GridDefinition.TRACE_INDEX];
            _vol_eval = new VolumeEvaluator(_seisio);
            long total = _vol_eval.getTotal();
            long[] fts = new long[size];
            for (k2 = 0; k2 < size; k2++) {
                fts[k2] = getFirstTraceInFrame(k2, size, _vol_eval);
            }
            long[] lts = new long[size];
            for (k2 = 1; k2 < size; k2++) {
                lts[k2 - 1] = fts[k2] - 1;
            }
            lts[size - 1] = total - 1;
            if (_cur_rank > 0) {
                if (fts[_cur_rank] < lts[_cur_rank - 1]) {
                    fts[_cur_rank] = lts[_cur_rank - 1] + 1;
                }
            }
            _first_trace = fts[_cur_rank];
            _last_trace = lts[_cur_rank];
            _work_to_do = _first_trace < _last_trace;
            if (frame_count < 1) frame_count = 1;
            _frame_buffer = new FrameBuffer(this, frame_count);
            if (_work_to_do) {
                _first_frame = _first_trace / _traces_in_frame;
                _last_frame = _last_trace / _traces_in_frame;
            }
        }
    }
