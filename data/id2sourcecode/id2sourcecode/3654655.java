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
            String chanStr = null;
            if (channelName != null) {
                Object dynValue = resolveDynamicValue(channelName, context);
                chanStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            }
            if (StringUtils.isNotBlank(chanStr)) {
                if (debugLog.isLoggable(Level.FINEST)) debug("ExtenSpy trying to spy on " + chanStr);
            }
            Object dynValue = resolveDynamicValue(extension, context);
            String extStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            String ctxStr = null;
            if (context != null) {
                dynValue = resolveDynamicValue(this.context, context);
                ctxStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            }
            if (StringUtils.isBlank(chanStr) && StringUtils.isBlank(extStr)) {
                exception = new ActionStepException("Channel name or extension must be specified");
            } else {
                StringBuffer args = new StringBuffer();
                if (StringUtils.isNotBlank(chanStr)) args.append(chanStr); else {
                    args.append(extStr);
                    if (StringUtils.isNotBlank(ctxStr)) args.append('@').append(ctxStr);
                }
                args.append("|v(").append(volume).append(')');
                if (spyBridgedOnly) args.append('b');
                if (StringUtils.isNotBlank(group)) args.append("g(").append(group).append(')');
                if (!beep) args.append('q');
                if (StringUtils.isNotBlank(recordFilenamePrefix)) args.append("r(").append(recordFilenamePrefix).append(')');
                if (whisperEnabled) args.append('w');
                if (privateWhisperEnabled) args.append('W');
                if (debugLog.isLoggable(Level.FINEST)) debug("Executing ExtenSpy app with args " + args);
                int result = channel.exec("ExtenSpy", args.toString());
                if (debugLog.isLoggable(Level.FINEST)) debug("ExtenSpy return value was " + translateAppReturnValue(result));
                if (result == -1) {
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
