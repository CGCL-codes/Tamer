    @Override
    public void initialize(InitiatorInfo info) throws ActionStepException {
        super.initialize(info);
        if (newCall1 == null) throw new ActionStepException("No call found for IncomingCall initiator " + getName());
        if (!(newCall1 instanceof Call)) {
            throw new ActionStepException("Call isn't isn't an Asterisk call: " + newCall1.getClass().getName());
        }
        if (((Call) newCall1).getChannel() == null) {
            new ActionStepException("No channel found in current context");
        }
        AgiRequest request = ((AsteriskInitiatorInfo) info).getRequest();
        AgiChannel channel = ((AsteriskInitiatorInfo) info).getChannel();
        AsteriskServer server = ((AsteriskInitiatorInfo) info).getAsteriskServer();
        ManagerConnection connection = ((AsteriskInitiatorInfo) info).getManagerConnection();
        Saflet handler = getSaflet();
        SafletContext context = handler.getSafletContext();
        if (request != null) {
            Variable requestVar = context.getVariable(AsteriskSafletConstants.VAR_KEY_REQUEST);
            if (requestVar == null) {
                requestVar = DbFactory.eINSTANCE.createVariable();
                requestVar.setName(AsteriskSafletConstants.VAR_KEY_REQUEST);
            }
            requestVar.setName(AsteriskSafletConstants.VAR_KEY_REQUEST);
            requestVar.setType(VariableType.OBJECT);
            requestVar.setScope(VariableScope.RUNTIME);
            requestVar.setDefaultValue(request);
            context.addOrUpdateVariable(requestVar);
        }
        if (channel != null) {
            Variable channelVar = DbFactory.eINSTANCE.createVariable();
            channelVar.setName(AsteriskSafletConstants.VAR_KEY_CHANNEL);
            channelVar.setType(VariableType.OBJECT);
            channelVar.setScope(VariableScope.RUNTIME);
            channelVar.setDefaultValue(channel);
            context.addOrUpdateVariable(channelVar);
        }
        if (connection != null) {
            Variable connectionVar = DbFactory.eINSTANCE.createVariable();
            connectionVar.setName(AsteriskSafletConstants.VAR_KEY_MANAGER_CONNECTION);
            connectionVar.setType(VariableType.OBJECT);
            connectionVar.setScope(VariableScope.RUNTIME);
            connectionVar.setDefaultValue(connection);
            context.addOrUpdateVariable(connectionVar);
        }
        if (server != null) {
            Variable serverVar = DbFactory.eINSTANCE.createVariable();
            serverVar.setName(SafletConstants.VAR_KEY_TELEPHONY_SUBSYSTEM);
            serverVar.setType(VariableType.OBJECT);
            serverVar.setScope(VariableScope.RUNTIME);
            serverVar.setDefaultValue(server);
            context.addOrUpdateVariable(serverVar);
        }
        ((Call) newCall1).setCallerIdName(request.getCallerIdName());
        ((Call) newCall1).setCallerIdNum(request.getCallerIdNumber());
        ((Call) newCall1).setChannel(channel);
        ((Call) newCall1).setChannelName(channel.getName());
        ((Call) newCall1).setUniqueId(channel.getUniqueId());
    }
