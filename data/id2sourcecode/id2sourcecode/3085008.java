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
            StringBuffer appCmd = new StringBuffer("Ringing");
            int result = channel.exec(appCmd.toString());
            if (debugLog.isLoggable(Level.FINEST)) debug("Ringing return value was " + translateAppReturnValue(result));
            if (result == -1) {
                exception = new ActionStepException("Channel was hung up");
            } else {
                result = channel.exec("Wait", String.valueOf(duration / 1000));
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
