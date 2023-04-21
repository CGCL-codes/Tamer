    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Object variableRawValue = context.getVariableRawValue(AsteriskSafletConstants.VAR_KEY_MANAGER_CONNECTION);
        if (variableRawValue == null || !(variableRawValue instanceof ManagerConnection)) {
            handleException(context, new ActionStepException("No manager connection found in current context"));
            return;
        }
        ManagerConnection connection = (ManagerConnection) variableRawValue;
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
        Exception exception = null;
        try {
            MonitorAction action = new MonitorAction();
            String filename = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(filenamePrefix, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("Monitor recording to filename with prefix: " + filename);
            action.setFile(filename);
            action.setFormat(format);
            action.setChannel(((Call) call1).getChannelName());
            action.setMix(mix);
            ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
            if (debugLog.isLoggable(Level.FINEST)) debug("Monitor returned " + response.getMessage() + " of type " + response.getResponse());
            if (response instanceof ManagerError) exception = new ActionStepException("Couldn't monitor channel: " + response.getMessage());
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context);
    }
