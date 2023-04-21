    public Object processMessage(ClientMessage message) throws Throwable {
        ServerSession handler = getServerSession();
        if (handler == null) {
            throw new CayenneRuntimeException("No session associated with request.");
        }
        logObj.debug("processMessage, sessionId: " + handler.getSession().getSessionId());
        try {
            return DispatchHelper.dispatch(handler.getChannel(), message);
        } catch (Throwable th) {
            th = Util.unwindException(th);
            logObj.info("error processing message", th);
            throw th;
        }
    }
