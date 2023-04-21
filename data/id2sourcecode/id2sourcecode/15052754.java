    private SynchronizedLeaveBlockSyncPoint.Translated processSynchronizedLeaveBlockSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
        Field objectField = entryObjType.fieldByName("_object");
        if (null == objectField) {
            throw new Error("Could not find _object field.");
        }
        Value objectValue = entryObj.getValue(objectField);
        if (!(objectValue instanceof ObjectReference)) {
            throw new Error("Unexpected type for _object: " + objectValue);
        }
        ObjectReference objectRef = (ObjectReference) objectValue;
        LockInfo li = new LockInfo(objectRef);
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
        _writer.printf("%5d: leave block, lock = %d%s", ti.getThreadID(), li.getUniqueId(), System.getProperty("line.separator"));
        _writer.flush();
        LockInfo liOld = _locks.get(li.getUniqueId());
        if (null == liOld) {
            _locks.put(li.getUniqueId(), li);
            liOld = li;
        }
        liOld.setOwningThreadId(null);
        ThreadInfo tiOld = _threads.get(ti.getThreadID());
        if (null == tiOld) {
            _threads.put(ti.getThreadID(), ti);
            tiOld = ti;
        }
        tiOld.getOwnedLockIDs().remove(liOld.getUniqueId());
        return new SynchronizedLeaveBlockSyncPoint.Translated(li, ti);
    }
