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
        Exception exception = null;
        int idx = 0;
        try {
            Object dynValue = resolveDynamicValue(this.context, context);
            String ctx = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            dynValue = resolveDynamicValue(extension, context);
            String ext = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            if (StringUtils.isBlank(ext)) throw new ActionStepException("Extension is required for Extension Transfer ActionStep");
            Object variableRawValue = context.getVariableRawValue(AsteriskSafletConstants.VAR_KEY_MANAGER_CONNECTION);
            ManagerConnection connection = null;
            if (variableRawValue != null && variableRawValue instanceof ManagerConnection) connection = (ManagerConnection) variableRawValue;
            boolean gotExtenInfo = false;
            if (isDoPreExtenStatusCheck() && connection != null) {
                int status = Integer.MIN_VALUE;
                ExtensionStateAction action = new ExtensionStateAction(ext, ctx == null ? "" : ctx);
                ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
                if (response instanceof ExtensionStateResponse) {
                    ExtensionStateResponse resp = (ExtensionStateResponse) response;
                    gotExtenInfo = true;
                    status = resp.getStatus();
                }
                if (gotExtenInfo) {
                    switch(status) {
                        case 0:
                            break;
                        case 1:
                        case 2:
                        case 8:
                        case 16:
                            idx = 2;
                            break;
                        case -1:
                        case 4:
                            idx = 6;
                    }
                }
            }
            if (idx == 0) {
                dynValue = resolveDynamicValue(channelType, context);
                String type = StringUtils.trim((String) VariableTranslator.translateValue(VariableType.TEXT, dynValue));
                if (StringUtils.isBlank(type)) type = "Local/"; else if (!type.endsWith("/")) type += "/";
                String options = type + ext;
                if (StringUtils.isNotBlank(ctx)) options += "@" + ctx;
                if (timeout > 0) options += "|" + timeout;
                if (this.options != null) {
                    dynValue = resolveDynamicValue(this.options, context);
                    String addnlOptions = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
                    if (StringUtils.isNotBlank(addnlOptions)) {
                        options += "|" + addnlOptions;
                    }
                }
                if (debugLog.isLoggable(Level.FINEST)) debug("About to dial " + options);
                channel.exec("Dial", options);
                String status = channel.getVariable("DIALSTATUS");
                if (debugLog.isLoggable(Level.FINEST)) debug("Dial returned status " + status);
                if (status == null) idx = 0; else {
                    status = status.trim();
                    if ("BUSY".equals(status)) {
                        idx = 2;
                    } else if ("NOANSWER".equals(status)) {
                        idx = 3;
                    } else if ("CANCEL".equals(status)) {
                        idx = 4;
                    } else if ("DONTCALL".equals(status)) {
                        idx = 5;
                    } else if ("CHANUNAVAIL".equals(status)) {
                        idx = 6;
                    } else if ("ANSWER".equals(status)) {
                        idx = 1;
                    } else idx = 0;
                }
            }
            if (debugLog.isLoggable(Level.FINEST)) debug("index is " + idx);
        } catch (Exception e) {
            exception = e;
        }
        if (exception != null) {
            handleException(context, exception);
            return;
        }
        handleSuccess(context, idx);
    }
