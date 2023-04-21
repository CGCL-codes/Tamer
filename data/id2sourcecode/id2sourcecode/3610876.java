    public Object sendRequest(String oid, Type type, String operation, Object[] params) throws Throwable {
        Object result = null;
        checkDisposed();
        boolean goThroughThreadPool = false;
        ThreadId threadId = _iThreadPool.getThreadId();
        Object handle = _iThreadPool.attach(threadId);
        try {
            boolean sync;
            try {
                sync = _iProtocol.writeRequest(oid, TypeDescription.getTypeDescription(type), operation, threadId, params);
            } catch (IOException e) {
                DisposedException d = new DisposedException(e.toString());
                dispose(d);
                throw d;
            }
            if (sync && Thread.currentThread() != _messageDispatcher) {
                result = _iThreadPool.enter(handle, threadId);
            }
        } finally {
            _iThreadPool.detach(handle, threadId);
            if (operation.equals("release")) release();
        }
        if (DEBUG) System.err.println("##### " + getClass().getName() + ".sendRequest left:" + result);
        if (operation.equals("queryInterface") && result instanceof Any) {
            Any a = (Any) result;
            if (a.getType().getTypeClass() == TypeClass.INTERFACE) {
                result = a.getObject();
            } else {
                result = null;
            }
        }
        return result;
    }
