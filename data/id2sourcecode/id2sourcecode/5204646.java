    private void processSyncPoint(long tid, long code, long classIndex, long methodAndPC, long oid, HashMap<String, String> methodDatabase) {
        CompactThreadInfo cti = _threads.get(tid);
        if (cti == null) {
            cti = new CompactThreadInfo(tid, _methodDatabase);
        }
        cti.setLastClassIndex(classIndex);
        cti.setLastMethodIndexAndPC(methodAndPC);
        long methodIndex = (methodAndPC >>> 32);
        long methodPC = (methodAndPC & 0xFFFF);
        String methodAndPCString = "";
        if ((classIndex != 0) && (methodIndex != 0)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(baos);
            pw.printf("%08x %04x", classIndex, methodIndex);
            pw.flush();
            String key = baos.toString();
            methodAndPCString = methodDatabase.get(key);
            if (methodAndPCString == null) {
                methodAndPCString = "";
            } else {
                methodAndPCString = " " + methodAndPCString + " PC=" + methodPC;
            }
        }
        String prefix = "";
        if ((((Boolean) _record.getProperty("mainEntered")) == false) || (((Integer) _record.getProperty("userThreads")) == 0)) {
            prefix = "// ";
        }
        _writer.printf("%s%d %d // %08x %04x %04x %10d%s%s", prefix, tid, code, classIndex, methodIndex, methodPC, oid, methodAndPCString, System.getProperty("line.separator"));
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
            if (oid != 0) {
                if (!cti.getOwnedLockIDs().keySet().contains(oid)) {
                    cti.setContendedLockID(oid);
                    cti.setStatus(CompactThreadInfo.STATUS.WAITING);
                }
            } else {
                _writer.println("// Warning: encountered TRYMONITORENTER with object ID = 0");
            }
        } else if (code == SyncPointBuffer.SP.MONITORENTER.intValue()) {
            if (oid != 0) {
                if (!cti.getOwnedLockIDs().keySet().contains(oid)) {
                    cti.getOwnedLockIDs().put(oid, 1L);
                    cti.setContendedLockID(null);
                    cti.setStatus(CompactThreadInfo.STATUS.RUNNING);
                } else {
                    long count = cti.getOwnedLockIDs().get(oid);
                    cti.getOwnedLockIDs().put(oid, count + 1);
                }
            } else {
                _writer.println("// Warning: encountered MONITORENTER with object ID = 0");
            }
        } else if (code == SyncPointBuffer.SP.MONITOREXIT.intValue()) {
            if (oid != 0) {
                long count = cti.getOwnedLockIDs().get(oid);
                if (count == 1) {
                    cti.getOwnedLockIDs().remove(oid);
                } else {
                    cti.getOwnedLockIDs().put(oid, count - 1);
                }
            } else {
                _writer.println("// Warning: encountered MONITOREXIT with object ID = 0");
            }
        }
        _threads.put(tid, cti);
    }
