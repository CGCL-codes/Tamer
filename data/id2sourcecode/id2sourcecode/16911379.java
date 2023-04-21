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
            String conferenceNum = (String) VariableTranslator.translateValue(VariableType.TEXT, resolveDynamicValue(conferenceNumber, context));
            if (debugLog.isLoggable(Level.FINEST)) debug("Getting meetme count for conference: " + conferenceNum);
            if (StringUtils.isBlank(conferenceNum)) {
                exception = new ActionStepException("Conference number is required for MeetMeCount");
            } else {
                Variable v = resolveVariableFromName(variableName, context);
                StringBuffer appCmd = new StringBuffer();
                appCmd.append(conferenceNum);
                if (v != null) {
                    appCmd.append('|').append(MEETME_COUNT_VARNAME);
                }
                int result = channel.exec("MeetMeCount", appCmd.toString());
                if (debugLog.isLoggable(Level.FINEST)) debug("MeetMeCount returned " + translateAppReturnValue(result) + " of int " + result);
                if (result == -2) {
                    exception = new ActionStepException("Application MeetMeCount not found");
                } else if (result == -1) {
                    exception = new ActionStepException("Channel was hung up");
                } else if (v != null) {
                    String count = channel.getVariable(MEETME_COUNT_VARNAME);
                    if (debugLog.isLoggable(Level.FINEST)) {
                        debug("MEET_ME_COUNT var was " + count);
                    }
                    int meetmeCount = -1;
                    if (count != null) {
                        try {
                            meetmeCount = Integer.parseInt(count);
                            if (debugLog.isLoggable(Level.FINEST)) {
                                debug("MeetMe count was " + meetmeCount);
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                    if (v.getScope() != VariableScope.GLOBAL) context.setVariableRawValue(v.getName(), VariableTranslator.translateValue(v.getType(), meetmeCount)); else {
                        SafletEnvironment env = getSaflet().getSafletEnvironment();
                        env.setGlobalVariableValue(v.getName(), VariableTranslator.translateValue(v.getType(), meetmeCount));
                    }
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
