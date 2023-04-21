    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        Exception exception = null;
        int idx = 1;
        ManagerConnection connection = (ManagerConnection) context.getVariableRawValue(AsteriskSafletConstants.VAR_KEY_MANAGER_CONNECTION);
        if (connection == null) {
            exception = new ActionStepException("No manager connection found in current context");
        }
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
        if (StringUtils.isBlank(((Call) call1).getChannelName())) {
            exception = new ActionStepException(call1 == null ? "No current call found" : "No channel name set");
        }
        {
            try {
                Object dynValue = resolveDynamicValue(digits, context);
                String digitsStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
                if (StringUtils.isBlank(digitsStr)) exception = new ActionStepException("No digits specified"); else {
                    PlayDtmfAction action = new PlayDtmfAction();
                    action.setChannel(((Call) call1).getChannelName());
                    for (int i = 0; i < digitsStr.length(); i++) {
                        char ch = digitsStr.charAt(i);
                        if (',' == ch) {
                            try {
                                Thread.sleep(300);
                            } catch (Exception e) {
                            }
                            continue;
                        } else if (!(ch == '#' || ch == '*' || Character.isDigit(ch))) {
                            if (debugLog.isLoggable(Level.FINEST)) getSaflet().warn("skipping char " + ch);
                        }
                        action.setDigit(String.valueOf(ch));
                        ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
                        if (response instanceof ManagerError) {
                            exception = new ActionStepException("Couldn't redirect call to extension: " + response);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                exception = e;
            }
        }
        if (exception != null) {
            if (exception instanceof TimeoutException) {
                idx = 2;
            } else {
                handleException(context, exception);
                return;
            }
        }
        handleSuccess(context, idx);
    }
