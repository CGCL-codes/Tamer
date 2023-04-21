    private ThreadEnterSleepSyncPoint.Translated processThreadEnterSleepSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
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
        Field durField = entryObjType.fieldByName("_duration");
        if (null == durField) {
            throw new Error("Could not find _duration field.");
        }
        Value durValue = entryObj.getValue(durField);
        if (!(durValue instanceof LongValue)) {
            throw new Error("Unexpected type for _duration.");
        }
        _writer.printf("%5d: Thread.sleep(%d)%s", ti.getThreadID(), ((LongValue) durValue).longValue(), System.getProperty("line.separator"));
        _writer.flush();
        ThreadInfo tiOld = _threads.get(ti.getThreadID());
        if (null == tiOld) {
            _threads.put(ti.getThreadID(), ti);
            tiOld = ti;
        }
        tiOld.setStatus(ThreadReference.THREAD_STATUS_SLEEPING);
        return new ThreadEnterSleepSyncPoint.Translated(ti, ((LongValue) durValue).longValue());
    }
