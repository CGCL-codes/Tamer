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
            String channelName = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(channelnamePrefix, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("Spying on channels starting with: " + channelName);
            StringBuffer appCmd = new StringBuffer();
            if (StringUtils.isNotBlank(channelName)) appCmd.append(channelName);
            appCmd.append('|');
            if (spyBridgedOnly) appCmd.append('b');
            if (StringUtils.isNotBlank(group)) appCmd.append("g(").append(group).append(')');
            if (!beep) appCmd.append('q');
            if (StringUtils.isNotBlank(recordFilenamePrefix)) appCmd.append("r[").append(recordFilenamePrefix).append(']');
            if (volume != 0) appCmd.append("v(").append(Math.min(4, Math.max(-4, volume))).append(')');
            if (privateWhisperEnabled) appCmd.append('W'); else if (whisperEnabled) appCmd.append('w');
            int result = channel.exec("Chanspy", appCmd.toString());
            if (debugLog.isLoggable(Level.FINEST)) debug("ChanSpy returned " + translateAppReturnValue(result) + " of int " + result);
            if (result == -2) {
                exception = new ActionStepException("Application ChansSpy not found");
            } else if (result == -1) {
                exception = new ActionStepException("Channel was hung up");
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
