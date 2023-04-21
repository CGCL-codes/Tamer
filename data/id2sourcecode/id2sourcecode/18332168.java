    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        int idx = 1;
        Object variableRawValue = context.getVariableRawValue(AsteriskSafletConstants.VAR_KEY_MANAGER_CONNECTION);
        if (variableRawValue == null || !(variableRawValue instanceof ManagerConnection)) exception = new ActionStepException("No manager connection found in current context"); else {
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
            AgiChannel channel = ((Call) call1).getChannel();
            try {
                MonitorAction action = new MonitorAction();
                Object dynValue = resolveDynamicValue(filename, context);
                String filename = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
                action.setFile(filename);
                action.setFormat(format);
                action.setMix(mix);
                String chan = channel.getName();
                action.setChannel(chan);
                ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
                if (response instanceof ManagerError) exception = new ActionStepException("Couldn't redirect call to extension: " + response);
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            if (exception instanceof TimeoutException) {
                idx = 2;
            } else {
                handleException(context, exception);
                return;
            }
        }
        handleSuccess(context, idx);
    }
