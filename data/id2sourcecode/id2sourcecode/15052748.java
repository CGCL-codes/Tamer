    private ObjectEnterWaitSyncPoint.Translated processObjectEnterWaitSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
        Field objectField = entryObjType.fieldByName("_object");
        if (null == objectField) {
            throw new Error("Could not find _object field.");
        }
        Value objectValue = entryObj.getValue(objectField);
        if (!(objectValue instanceof ObjectReference)) {
            throw new Error("Unexpected type for _object: " + objectValue);
        }
        ObjectReference objectRef = (ObjectReference) objectValue;
        Field timeOutField = entryObjType.fieldByName("_timeOut");
        if (null == timeOutField) {
            throw new Error("Could not find _timeOut field.");
        }
        Value timeOutValue = entryObj.getValue(timeOutField);
        if (!(timeOutValue instanceof LongValue)) {
            throw new Error("Unexpected type for _timeOut.");
        }
        Field threadField = entryObjType.fieldByName("_thread");
        if (null == threadField) {
            throw new Error("Could not find _thread field.");
        }
        Value threadValue = entryObj.getValue(threadField);
        if ((threadValue != null) && !(threadValue instanceof ThreadReference)) {
            throw new Error("Unexpected type for _thread: " + threadValue);
        }
        ThreadReference threadRef = (ThreadReference) threadValue;
        LockInfo li = new LockInfo(objectRef);
        ThreadInfo ti = new ThreadInfo(threadRef);
        _writer.printf("%5d: Object.wait, lock = %d%s", ti.getThreadID(), li.getUniqueId(), System.getProperty("line.separator"));
        _writer.flush();
        ThreadInfo tiOld = _threads.get(ti.getThreadID());
        if (null != tiOld) {
            tiOld.setStatus(ThreadReference.THREAD_STATUS_WAIT);
        }
        return new ObjectEnterWaitSyncPoint.Translated(li, ((LongValue) timeOutValue).longValue(), ti);
    }
