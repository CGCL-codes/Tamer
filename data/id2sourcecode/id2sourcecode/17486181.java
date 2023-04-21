    protected boolean writeOldestFrame() throws SeisException {
        if (!_read_write) {
            throw new SeisException("Attempt to write when ReadOnly");
        }
        boolean retval = true;
        int count;
        int frame_save = _frame_buffer.getFrameIndex();
        int frame = _frame_buffer.oldestModifiedFrame();
        _frame_buffer.setFrameIndex(frame);
        if (_frame_buffer.couldHaveChanged()) {
            count = _frame_buffer.writeFrame();
            retval = count > 0;
        }
        _frame_buffer.setFrameIndex(frame_save);
        return retval;
    }
