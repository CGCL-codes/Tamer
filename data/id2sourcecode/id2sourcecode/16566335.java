    private void processSyncPoint(long tid, long code, long classIndex, long methodAndPC, long oid, long[] stackTrace) {
        long methodIndex = (methodAndPC >>> 32);
        long methodPC = (methodAndPC & 0xFFFF);
        String methodAndPCString = "";
        if ((classIndex != 0) && (methodIndex != 0)) {
            long key = (classIndex << 32) | methodIndex;
            methodAndPCString = _methodDatabase.get(key);
            if (methodAndPCString == null) {
                methodAndPCString = "";
            } else {
                methodAndPCString = " " + methodAndPCString + " PC=" + methodPC;
            }
            if (_enableCounts) {
                long[] count = _methodCounts.get(key);
                if (count == null) {
                    count = new long[METHOD_COUNTS_COLUMNS];
                }
                count[METHOD_COUNTS_INDIVIDUAL] += 1;
                _methodCounts.put(key, count);
            }
        }
        if (_enableSyncPoints) {
            _writer.printf("%d %d // %08x %04x %04x %10d%s%s", tid, code, classIndex, methodIndex, methodPC, oid, methodAndPCString, System.getProperty("line.separator"));
        }
        if (_enableStackTraces || _enableCallTrees) {
            CallTree.StackTraceElement[] stes = new CallTree.StackTraceElement[stackTrace.length];
            int index = 0;
            for (long classAndMethodIdx : stackTrace) {
                long classIdx = classAndMethodIdx >>> 32;
                long methodIdx = classAndMethodIdx & 0x00000000ffffffffL;
                String methodString = _methodDatabase.get(classAndMethodIdx);
                if (methodString == null) {
                    methodString = "???";
                    stes[index] = new CallTree.StackTraceElement("???", "???", "", -1, classAndMethodIdx);
                } else {
                    try {
                        int lastDot = methodString.lastIndexOf('.');
                        String cname = methodString.substring(0, lastDot);
                        String mname = methodString.substring(lastDot + 1);
                        stes[index] = new CallTree.StackTraceElement(cname, mname, "", -1, classAndMethodIdx);
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println(methodString);
                    }
                }
                if (_enableStackTraces) {
                    _writer.printf("\t// at %08x %04x %s%s", classIdx, methodIdx, methodString, System.getProperty("line.separator"));
                }
                ++index;
            }
            if (_enableCallTrees) {
                _callTree.addStackTrace(stes);
            }
        }
        if (code == SyncPointBuffer2.SP.THREADSTART.intValue()) {
            ++_userThreads;
            if (_enableSyncPoints) {
                _writer.println("// user thread with id " + tid + " started; " + _userThreads + " remaining");
            }
        } else if (code == SyncPointBuffer2.SP.THREADEXIT.intValue()) {
            --_userThreads;
            if (_enableSyncPoints) {
                _writer.println("// user thread with id " + tid + " exited; " + _userThreads + " remaining");
            }
        }
        ++_numTotalSyncPoints;
    }
