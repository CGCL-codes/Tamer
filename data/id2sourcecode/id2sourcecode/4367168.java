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
            Variable v = resolveVariableFromName(variableName, context);
            StringBuilder buf = new StringBuilder();
            boolean rcvdEscape = false;
            int numRemainingDigits = maxDigits;
            if (useBufferedDigits) {
                for (int i = 0; i < maxDigits; i++) {
                    char next = ((AsteriskSafletContext) context).popBufferedDigit();
                    if (next == 253) break;
                    if (escapeDigits.indexOf(next) >= 0) {
                        rcvdEscape = true;
                        break;
                    }
                    numRemainingDigits--;
                    buf.append(next);
                }
            } else ((AsteriskSafletContext) context).flushBufferedDigits();
            if (!rcvdEscape && numRemainingDigits > 0) {
                if ("#".equals(escapeDigits)) buf.append(channel.getData("silence/1", inputTimeout, numRemainingDigits));
                for (int i = buf.length(); i < maxDigits; i++) {
                    char c = channel.waitForDigit((int) inputTimeout);
                    if (StringUtils.isNotBlank(escapeDigits) && escapeDigits.indexOf(c) >= 0) {
                        idx = 1;
                        break;
                    }
                    if (c != 0) {
                        String digitPressed = String.valueOf(c);
                        buf.append(digitPressed);
                    } else {
                        break;
                    }
                }
            }
            if (buf.length() > 0) idx = 1; else idx = 2;
            if (v != null) {
                if (v.getScope() != VariableScope.GLOBAL) context.setVariableRawValue(v.getName(), VariableTranslator.translateValue(v.getType(), buf.toString())); else {
                    SafletEnvironment env = getSaflet().getSafletEnvironment();
                    env.setGlobalVariableValue(v.getName(), VariableTranslator.translateValue(v.getType(), buf.toString()));
                }
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
