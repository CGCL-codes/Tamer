    private ThreadStartSyncPoint.Translated processThreadStartSyncPoint(ReferenceType entryObjType, ObjectReference entryObj) {
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
        _writer.printf("%5d: Thread.start%s", ti.getThreadID(), System.getProperty("line.separator"));
        _writer.flush();
        _threads.put(ti.getThreadID(), ti);
        return new ThreadStartSyncPoint.Translated(ti);
    }
