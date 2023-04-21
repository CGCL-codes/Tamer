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
            Object dynValue = resolveDynamicValue(filename, context);
            String filenameStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            if (StringUtils.isBlank(filenameStr)) exception = new ActionStepException("Filename was null"); else {
                if (filename.getType() == DynamicValueType.CUSTOM) filenameStr = getSaflet().getPromptPathByName(filenameStr);
                if (debugLog.isLoggable(Level.FINEST)) debug("Streaming file " + filenameStr);
                char c = channel.streamFile(filenameStr, escapeDigits);
                if (c > 0) ((AsteriskSafletContext) context).appendBufferedDigits(String.valueOf(c));
                if (debugLog.isLoggable(Level.FINEST)) debug("StreamAudio returned " + translateAppReturnValue(c));
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
