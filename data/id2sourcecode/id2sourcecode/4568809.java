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
            String txt = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(text, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("Festival about to vocalize: " + txt);
            StringBuffer appCmd = new StringBuffer(txt);
            if (StringUtils.isNotBlank(interruptKeys)) appCmd.append(",").append(interruptKeys);
            int result = channel.exec("Festival", appCmd.toString());
            if (debugLog.isLoggable(Level.FINEST)) debug("Festival returned " + translateAppReturnValue(result) + " of int " + result);
            if (result == -2) {
                exception = new ActionStepException("Application Festival not found");
            } else if (result == -1) {
                exception = new ActionStepException("Channel was hung up or command failed");
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
