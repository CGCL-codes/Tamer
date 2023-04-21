    private void processSyncPoint(long tid, long code) {
        CompactThreadInfo cti = _threads.get(tid);
        if (cti == null) {
            cti = new CompactThreadInfo(tid, _methodDatabase);
        }
        String prefix = "";
        if ((((Boolean) _record.getProperty("mainEntered")) == false) || (((Integer) _record.getProperty("userThreads")) == 0)) {
            prefix = "// ";
        }
        _writer.printf("%s%d %d%s", prefix, tid, code, System.getProperty("line.separator"));
        if (code == SyncPointBuffer.SP.THREADSTART.intValue()) {
            int userThreads = ((Integer) _record.getProperty("userThreads")) + 1;
            _record.setProperty("userThreads", userThreads);
            _writer.println("// user thread with id " + tid + " started; " + userThreads + " remaining");
            cti.setStatus(CompactThreadInfo.STATUS.RUNNING);
        } else if (code == SyncPointBuffer.SP.THREADEXIT.intValue()) {
            int userThreads = ((Integer) _record.getProperty("userThreads")) - 1;
            _record.setProperty("userThreads", userThreads);
            _writer.println("// user thread with id " + tid + " exited; " + userThreads + " remaining");
            if (userThreads == 0) {
                _record.setAllUserThreadsDied();
            }
            cti.setStatus(CompactThreadInfo.STATUS.DEAD);
        } else if (code == SyncPointBuffer.SP.TRYMONITORENTER.intValue()) {
            cti.setStatus(CompactThreadInfo.STATUS.WAITING);
        } else if (code == SyncPointBuffer.SP.MONITORENTER.intValue()) {
            cti.setStatus(CompactThreadInfo.STATUS.RUNNING);
        } else if (code == SyncPointBuffer.SP.MONITOREXIT.intValue()) {
            cti.setStatus(CompactThreadInfo.STATUS.RUNNING);
        }
        _threads.put(tid, cti);
    }
