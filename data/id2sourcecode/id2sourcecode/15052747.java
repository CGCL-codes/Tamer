    private ThreadLeaveYieldSyncPoint.Translated processThreadLeaveYieldSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
        Field threadField = entryObjType.fieldByName("_thread");
        if (null == threadField) {
            throw new Error("Could not find _thread field.");
        }
        Value threadValue = entryObj.getValue(threadField);
        if ((threadValue != null) && !(threadValue instanceof ThreadReference)) {
            throw new Error("Unexpected type for _thread: " + threadValue);
        }
        ThreadReference threadRef = (ThreadReference) threadValue;
        ThreadInfo ti = new ThreadInfo(threadRef);
        _writer.printf("%5d: Thread.yield ended%s", ti.getThreadID(), System.getProperty("line.separator"));
        _writer.flush();
        ThreadInfo tiOld = _threads.get(ti.getThreadID());
        if (null == tiOld) {
            _threads.put(ti.getThreadID(), ti);
            tiOld = ti;
        }
        tiOld.setStatus(ThreadReference.THREAD_STATUS_RUNNING);
        return new ThreadLeaveYieldSyncPoint.Translated(ti);
    }
