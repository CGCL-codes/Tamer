    public int init() {
        boolean read_write;
        if (_rw_mode.equalsIgnoreCase("rw") || _rw_mode.equalsIgnoreCase("w")) {
            read_write = true;
        } else {
            read_write = false;
        }
        if (!read_write) {
            JavaSeisReader jsr = new JavaSeisReader(_path);
            if (!jsr.preExisted()) {
                _status = ERROR;
                _error_message = "JavaSeisWrapper.init: file not found " + _path;
            } else {
                if (!jsr.open() || jsr.status() == JavaSeisIO.ERROR) {
                    _status = ERROR;
                    _error_message = "JavaSeisWrapper.init: " + jsr.errorMessage();
                }
                _js = jsr;
            }
        } else {
            _jsw = (JSIO) (new JavaSeisWriter(_path));
            if (_jsw.preExisted()) {
                if (!_jsw.open() || _jsw.status() == JavaSeisIO.ERROR) {
                    _status = ERROR;
                    _error_message = "JavaSeisWrapper.init: " + _jsw.errorMessage();
                }
            }
            _js = (JavaSeisIO) _jsw;
        }
        if (_js == null) {
            _status = ERROR;
            _error_message = "JavaSeisWrapper.init: Failed";
        }
        return verifyStatus();
    }
