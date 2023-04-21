    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        int idx = 1;
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
            Object dynValue = resolveDynamicValue(filename, context);
            String filenameStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            {
                Variable v = null;
                try {
                    v = resolveVariableFromName(variableName, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(filenameStr) && filename.getType() == DynamicValueType.CUSTOM) filenameStr = getSaflet().getPromptPathByName(filenameStr);
                StringBuffer buf = new StringBuffer();
                boolean rcvdEscape = false;
                int numRemainingDigits = maxDigits;
                if (useBufferedDigits) {
                    for (int i = 0; i < maxDigits; i++) {
                        char next = ((AsteriskSafletContext) context).popBufferedDigit();
                        if (next == 253) break;
                        if (next == '#') {
                            rcvdEscape = true;
                            break;
                        }
                        numRemainingDigits--;
                        buf.append(next);
                    }
                } else ((AsteriskSafletContext) context).flushBufferedDigits();
                if (!rcvdEscape && numRemainingDigits > 0) {
                    buf.append(channel.getData(StringUtils.isBlank(filenameStr) || (useBufferedDigits && buf.length() > 0) ? "silence/1" : filenameStr, timeout, numRemainingDigits));
                }
                if (debugLog.isLoggable(Level.FINEST)) debug("Data returned was " + buf);
                if (StringUtils.isBlank(buf.toString()) || buf.toString().toLowerCase().indexOf("(timeout)") >= 0) {
                    if (debugLog.isLoggable(Level.FINEST)) info("Taking timeout path...");
                    idx = 2;
                } else if (v != null) {
                    if (v.getScope() != VariableScope.GLOBAL) context.setVariableRawValue(v.getName(), VariableTranslator.translateValue(v.getType(), buf.toString())); else {
                        SafletEnvironment env = getSaflet().getSafletEnvironment();
                        env.setGlobalVariableValue(v.getName(), VariableTranslator.translateValue(v.getType(), buf.toString()));
                    }
                } else {
                    String msg = "warning: variable not specified. Discarding digits " + buf.toString();
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.warning(msg);
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
