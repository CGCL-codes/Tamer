    public Object overrideCall(Object proxyId, String method, Object[] args, Class returnTyep) throws Exception {
        args = objectManager.obj2ids(args);
        Object sid = createSessionId();
        Object tid = getThreadId(sid);
        Thread ct = Thread.currentThread();
        Utils.writeArray(monitor, Level.DEBUG, new Object[] { "SessionManager  SID:", sid.toString(), "  TID:", tid.toString(), " [", ct.getName(), " in ", ct.getThreadGroup().getName(), "]" });
        try {
            CallSession session = new CallSession(sid, proxyId, method, args, returnTyep);
            sessionTable.put(sid, session);
            threadTable.put(ct, tid);
            return objectManager.id2obj(session.overrideCall(overrideCall));
        } finally {
            sessionTable.remove(sid);
        }
    }
