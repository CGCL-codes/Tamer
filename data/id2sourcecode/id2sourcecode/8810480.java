    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
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
        try {
            Object dynValue = resolveDynamicValue(arguments, context);
            String args = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            if (debugLog.isLoggable(Level.FINEST)) debug("Asterisk application is " + application + " with args " + args);
            Object appDynValue = resolveDynamicValue(application, context);
            String appname = (String) VariableTranslator.translateValue(VariableType.TEXT, appDynValue);
            if (StringUtils.isBlank(appname)) {
                exception = new ActionStepException("Asterisk Application was not specified!");
            } else {
                int result = channel.exec(appname, args);
                if (debugLog.isLoggable(Level.FINEST)) debug(application + " returned " + result);
                if (result == -2) exception = new ActionStepException("Asterisk Application " + application + " was not found!");
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
