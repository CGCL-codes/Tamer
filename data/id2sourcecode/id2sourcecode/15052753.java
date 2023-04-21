    private SynchronizedEnterBlockSyncPoint.Translated processSynchronizedEnterBlockSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
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
        _writer.printf("%5d: enter block, lock = %d%s", ti.getThreadID(), li.getUniqueId(), System.getProperty("line.separator"));
        _writer.flush();
        LockInfo liOld = _locks.get(li.getUniqueId());
        if (null == liOld) {
            _locks.put(li.getUniqueId(), li);
            liOld = li;
        }
        liOld.getWaitingThreadIds().remove(ti.getThreadID());
        liOld.setOwningThreadId(ti.getThreadID());
        ThreadInfo tiOld = _threads.get(ti.getThreadID());
        if (null == tiOld) {
            _threads.put(ti.getThreadID(), ti);
            tiOld = ti;
        }
        tiOld.getOwnedLockIDs().add(tiOld.getContendedLockID());
        tiOld.setContendedLockId(null);
        tiOld.setStatus(ThreadReference.THREAD_STATUS_RUNNING);
        return new SynchronizedEnterBlockSyncPoint.Translated(li, ti);
    }
