    private Exception takeControl(ManagerConnection connection, SafletContext context) throws ActionStepException, IOException, IllegalArgumentException, IllegalStateException, TimeoutException, AgiException {
        OriginateAction action = new OriginateAction();
        Object dynValue = resolveDynamicValue(this.channel, context);
        String chan = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
        if (StringUtils.isBlank(chan)) {
            return new ActionStepException("Channel name must be specified");
        }
        if (debugLog.isLoggable(Level.FINEST)) debug("Creating call from channel " + chan);
        action.setContext("default");
        action.setApplication("Agi");
        AsteriskServer server = (AsteriskServer) context.getVariableRawValue(SafletConstants.VAR_KEY_TELEPHONY_SUBSYSTEM);
        String serverAddr = server == null ? null : server.getVisibleSafiServerIP();
        if (StringUtils.isBlank(serverAddr)) serverAddr = connection.getLocalAddress().getCanonicalHostName();
        action.setData("agi://" + serverAddr + "/safletEngine.agi?loopback=true");
        action.setChannel(chan);
        action.setAsync(async);
        dynValue = resolveDynamicValue(callerId, context);
        String cid = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
        action.setCallerId(cid);
        dynValue = resolveDynamicValue(account, context);
        String acctVal = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
        action.setAccount(acctVal);
        UUID uuid = UUID.randomUUID();
        AsteriskSafletEnvironment handlerEnvironment = (AsteriskSafletEnvironment) getSaflet().getSafletEnvironment();
        Long timeoutVal = new Long(timeout <= 0 ? Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT : timeout);
        handlerEnvironment.setLoopbackLock(uuid.toString(), timeoutVal);
        action.setVariable("SafiUUID", uuid.toString());
        ManagerResponse response = connection.sendAction(action, Saflet.DEFAULT_MANAGER_ACTION_TIMEOUT);
        if (response instanceof ManagerError) return new ActionStepException("Couldn't place call: " + response.getMessage());
        Object returned = handlerEnvironment.getLoopbackCall(uuid.toString());
        if (returned instanceof Object[]) {
            Object[] pair = (Object[]) returned;
            if (newCall1 == null) setNewCall1(AsteriskFactoryImpl.eINSTANCE.createCall());
            ((Call) newCall1).setChannel((AgiChannel) pair[0]);
            ((Call) newCall1).setData("AgiRequest", pair[1]);
        } else {
            return new ActionStepException("Loopback for call failed!");
        }
        action.setCallingPres(callingPresentation);
        dynValue = resolveDynamicValue(this.context, context);
        String ctx = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
        ((Call) newCall1).getChannel().setContext(ctx);
        dynValue = resolveDynamicValue(extension, context);
        String ext = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
        ((Call) newCall1).getChannel().setExtension(ext);
        ((Call) newCall1).getChannel().setPriority(String.valueOf(priority));
        return null;
    }
