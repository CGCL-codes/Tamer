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
            String mb = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(mailbox, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("Getting VoicemailMain for mailbox: " + mb);
            if (StringUtils.isBlank(mb)) {
                exception = new ActionStepException("mailbox is required for VoicemailMain");
            } else {
                String folder = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(this.defaultFolder, context));
                if (StringUtils.isBlank(folder)) {
                    folder = null;
                }
                StringBuffer appCmd = new StringBuffer();
                appCmd.append(mb);
                if (skipPasswordCheck) appCmd.append("|s");
                if (usePrefix) appCmd.append("|p");
                if (recordingGain != 0) appCmd.append("|g(").append(recordingGain).append(")");
                if (folder != null) appCmd.append("|a(").append(folder).append(")");
                if (debugLog.isLoggable(Level.FINEST)) {
                    debug("sending: VoiceMailMain " + appCmd);
                }
                int result = channel.exec("VoiceMailMain", appCmd.toString());
                if (debugLog.isLoggable(Level.FINEST)) debug("VoiceMailMain returned " + translateAppReturnValue(result) + " of int " + result);
                if (result == -2) {
                    exception = new ActionStepException("Application VoiceMailMain not found");
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
