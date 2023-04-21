    public void programmingOpReply(int value, int retval) {
        if (log.isDebugEnabled()) log.debug("CV progOpReply for CV " + _num + " with retval " + retval + " during " + (_reading ? "read sequence" : (_confirm ? "confirm sequence" : "write sequence")));
        if (!_busy) log.error("opReply when not busy!");
        boolean oldBusy = _busy;
        if (retval == OK) {
            if (_status != null) _status.setText(rbt.getString("StateOK"));
            if (_reading) {
                _value = value;
                _tableEntry.setText(Integer.toString(value));
                notifyValueChange(value);
                setState(READ);
                if (log.isDebugEnabled()) log.debug("CV setting not busy on end read");
                _busy = false;
                notifyBusyChange(oldBusy, _busy);
            } else if (_confirm) {
                _decoderValue = value;
                if (value == _value) setState(SAME); else setState(DIFF);
                _busy = false;
                notifyBusyChange(oldBusy, _busy);
            } else {
                setState(STORED);
                _busy = false;
                notifyBusyChange(oldBusy, _busy);
            }
        } else {
            if (_status != null) _status.setText(java.text.MessageFormat.format(rbt.getString("StateProgrammerError"), new Object[] { mProgrammer.decodeErrorCode(retval) }));
            javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    errorTimeout();
                }
            });
            timer.setInitialDelay(1000);
            timer.setRepeats(false);
            timer.start();
        }
        if (log.isDebugEnabled()) log.debug("CV progOpReply end of handling CV " + _num);
    }
