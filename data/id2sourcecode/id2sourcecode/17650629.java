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
        AgiChannel channel = ((Call) call1).getChannel();
        Exception exception = null;
        try {
            RedirectAction action = new RedirectAction();
            Object dynValue = resolveDynamicValue(this.context, context);
            String ctx = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            action.setContext(ctx);
            action.setPriority(priority);
            dynValue = resolveDynamicValue(extension, context);
            String ext = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            action.setExten(ext);
            String chan = channel.getName();
            action.setChannel(chan);
            if (call2 != null) action.setExtraChannel(((Call) call2).getChannelName());
            StringBuffer buf = new StringBuffer();
            RedirectCallManagerEventListener eventListener = new RedirectCallManagerEventListener(buf, channel.getUniqueId());
            try {
                connection.addEventListener(eventListener);
                ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
                if (response instanceof ManagerError) exception = new ActionStepException("Couldn't redirect call to extension: " + response); else {
                    try {
                        synchronized (lock) {
                            if (!eventListener.stopped) {
                                lock.wait(10000);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                if (buf.length() == 0) {
                    exception = new ActionStepException("Call to " + ext + " failed to initiate");
                } else {
                    if (debugLog.isLoggable(Level.FINEST)) debug("The call was established " + buf);
                }
            } finally {
                connection.removeEventListener(eventListener);
                try {
                    eventListener.stop();
                } catch (Exception e) {
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
