    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
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
        boolean success = false;
        try {
            char c = Character.MAX_VALUE;
            while (true) {
                c = channel.waitForDigit((int) timeout);
                if (debugLog.isLoggable(Level.FINEST)) debug("Waitfordigit got " + c);
                if (c == 0) break;
                if (c != 0 && (acceptedDigits == null || acceptedDigits.indexOf(c) >= 0)) {
                    String digitPressed = String.valueOf(c);
                    ((AsteriskSafletContext) context).appendBufferedDigits(digitPressed);
                    success = true;
                    break;
                }
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context, success ? 1 : 2);
    }
