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
        try {
            Object result = resolveDynamicValue(digits, context);
            if (result != null && !StringUtils.isBlank(result.toString())) {
                char c = channel.sayDigits(result.toString(), escapeDigits);
                if (c != 0) {
                    String digitPressed = String.valueOf(c);
                    ((AsteriskSafletContext) context).appendBufferedDigits(digitPressed);
                }
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context);
    }
