    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        int idx = 0;
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
            if (debugLog.isLoggable(Level.FINEST)) debug("Getting Voicemail for mailbox: " + mb);
            if (StringUtils.isBlank(mb)) {
                exception = new ActionStepException("mailbox is required for Voicemail");
            } else {
                StringBuffer appCmd = new StringBuffer();
                appCmd.append(mb);
                if (skipInstructions) appCmd.append('|').append('s');
                if (playUnavailableMessage) {
                    appCmd.append('|');
                    appCmd.append('u');
                } else if (playBusyMessage) {
                    appCmd.append('|');
                    appCmd.append('b');
                }
                if (recordingGain != 0) {
                    appCmd.append('|');
                    appCmd.append("g(").append(recordingGain).append(")");
                }
                if (debugLog.isLoggable(Level.FINEST)) debug("sending VoiceMail " + appCmd);
                int result = channel.exec("VoiceMail", appCmd.toString());
                if (debugLog.isLoggable(Level.FINEST)) debug("VoiceMail returned " + translateAppReturnValue(result) + " of int " + result);
                if (result == -2) {
                    exception = new ActionStepException("Application VoiceMail not found");
                } else if (result == -1) {
                    exception = new ActionStepException("Channel was hung up");
                }
                String status = channel.getVariable("VMSTATUS");
                if (StringUtils.equalsIgnoreCase("SUCCESS", status)) idx = 1; else if (StringUtils.equalsIgnoreCase("USEREXIT", status)) idx = 2; else exception = new ActionStepException("Voicemail() returned failure: " + status);
            }
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context, idx);
    }
