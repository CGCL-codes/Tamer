    @Override
    public Object get(int index, Scriptable start) {
        Value value = ServerMethods.getProperty(sessionData.getChannel(), sessionData.getSessionHandler(), objectRef, index);
        return sessionData.getSessionHandler().makeJsvalFromValue(jsContext, value);
    }
