    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length < 2) {
            return Undefined.instance;
        }
        Value valueArgs[] = new Value[args.length - 2];
        for (int i = 0; i < valueArgs.length; i++) {
            valueArgs[i] = sessionData.getSessionHandler().makeValueFromJsval(cx, args[i + 2]);
        }
        Value thisValue = sessionData.getSessionHandler().makeValueFromJsval(cx, args[1]);
        int dispatchId = ((Number) args[0]).intValue();
        ExceptionOrReturnValue returnValue = getReturnFromJavaMethod(cx, sessionData.getSessionHandler(), sessionData.getChannel(), dispatchId, thisValue, valueArgs);
        Object ret[] = new Object[2];
        ret[0] = returnValue.isException();
        ret[1] = sessionData.getSessionHandler().makeJsvalFromValue(cx, returnValue.getReturnValue());
        return ret;
    }
