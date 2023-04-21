    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        boolean speechDetected = false;
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
        {
            AgiChannel channel = ((Call) call1).getChannel();
            try {
                StringBuffer appCmd = new StringBuffer(filename);
                appCmd.append('|').append(silence).append('|').append(minTime);
                if (maxTime > 0) appCmd.append('|').append(maxTime);
                int result = channel.exec("BackgroundDetect", appCmd.toString());
                if (debugLog.isLoggable(Level.FINEST)) {
                    debug("BackgroundDetect returned " + result);
                }
                if (result == -2) {
                    exception = new ActionStepException("Application BackgroundDetect not found");
                } else if (result == -1) {
                    context.addException(new ActionStepException("Channel was hung up"));
                    return;
                }
                String talk = channel.getVariable("TALK_DETECTED");
                if (debugLog.isLoggable(Level.FINEST)) {
                    debug("TALK_DETECTED var was " + talk);
                }
                if (talk != null) {
                    try {
                        int speechDuration = Integer.parseInt(talk);
                        if (speechDuration > 0) speechDetected = true;
                        if (debugLog.isLoggable(Level.FINEST)) {
                            debug("Speech duration was " + speechDuration);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context, speechDetected ? 2 : 1);
    }
