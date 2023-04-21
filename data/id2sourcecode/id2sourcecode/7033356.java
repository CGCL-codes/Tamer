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
        try {
            AgiRequest request = null;
            try {
                final Object val = context.getVariableRawValue(AsteriskSafletConstants.VAR_KEY_REQUEST);
                if (val != null && val instanceof AgiRequest) request = (AgiRequest) val;
            } catch (Exception e) {
            }
            if (accountCodeVar != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getAccountCode();
                    setVariableValue(accountCodeVar, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (callerIdNameVar != null) try {
                Object result = ((Call) call1).getCallerIdName();
                setVariableValue(callerIdNameVar, result, context);
            } catch (Exception e) {
                if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                exception = e;
            }
            if (callerIdNumVar != null) try {
                Object result = ((Call) call1).getCallerIdNum();
                setVariableValue(callerIdNumVar, result, context);
            } catch (Exception e) {
                if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                exception = e;
            }
            if (channelNameVar != null) try {
                Object result = ((Call) call1).getChannelName();
                setVariableValue(channelNameVar, result, context);
            } catch (Exception e) {
                if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                exception = e;
            }
            if (contextVar != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getContext();
                    setVariableValue(contextVar, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (extensionVar != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getExtension();
                    setVariableValue(extensionVar, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (dialedNumber != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getDnid();
                    setVariableValue(dialedNumber, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (priorityVar != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getPriority();
                    setVariableValue(priorityVar, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (stateVar != null) try {
                Object result = ((Call) call1).getChannel().getChannelStatus();
                setVariableValue(stateVar, result, context);
            } catch (Exception e) {
                if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                exception = e;
            }
            if (uniqueIdVar != null) try {
                Object result = ((Call) call1).getUniqueId();
                setVariableValue(uniqueIdVar, result, context);
            } catch (Exception e) {
                if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                exception = e;
            }
            if (rdnis != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getRdnis();
                    setVariableValue(rdnis, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (ani2Var != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getCallingAni2();
                    setVariableValue(ani2Var, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
            if (type != null) {
                if (request == null) throw new SafletException("No AgiRequest object available for current context");
                try {
                    Object result = request.getType();
                    setVariableValue(type, result, context);
                } catch (Exception e) {
                    if (debugLog.isLoggable(Level.FINEST)) debugLog.log(Level.SEVERE, e.getLocalizedMessage());
                    exception = e;
                }
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context);
    }
