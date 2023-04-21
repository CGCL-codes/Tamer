    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        if (call1 == null) {
            handleException(context, new ActionStepException("No current call found"));
            return;
        } else if (!(call1 instanceof Call)) {
            handleException(context, new ActionStepException("Call isn't isn't an Asterisk call: " + call1.getClass().getName()));
            return;
        }
        if (((Call) call1).getChannel() == null) {
            handleException(context, new ActionStepException("No channel found in current context"));
            return;
        }
        AgiChannel channel = ((Call) call1).getChannel();
        Exception exception = null;
        try {
            Object dynValue = resolveDynamicValue(holdClass, context);
            String holdClassStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            if (StringUtils.isBlank(holdClassStr)) {
                exception = new ActionStepException("Hold class was not specified.");
            } else {
                int result = channel.exec("SetMusicOnHold", holdClassStr);
                if (debugLog.isLoggable(Level.FINEST)) debug("SetMusicOnHold returned " + translateAppReturnValue(result) + " of int " + result);
                if (result == -2) {
                    exception = new ActionStepException("Application SetMusicOnHold not found");
                } else if (result == -1) {
                    exception = new ActionStepException("Channel was hung up");
                }
            }
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context);
    }
