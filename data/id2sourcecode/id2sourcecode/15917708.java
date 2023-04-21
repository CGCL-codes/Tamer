    public static void transfer(Event event) throws Exception {
        Agent agent = (Agent) UiUtil.getSessionAttribute("agent");
        if (agent == null) {
            ((Component) UiUtil.findComponent("topElement")).setVisible(false);
            UiUtil.showMessage(MessageType.ERROR, MessageUtil.getMessage(Agent.class, Messages.agent_6));
            return;
        }
        if (agent != null) {
            if (!agent.voiceEnabled) {
                return;
            }
            String targetType = "extension";
            String target = Converter.asString(UiUtil.getValue("DialNumber", "getValue"));
            if (NullStatus.isNull(target)) {
                target = Converter.asString(UiUtil.getValue("IVRID", "getValue"));
                if (NullStatus.isNotNull(target)) {
                    targetType = "IVR";
                }
            }
            if (NullStatus.isNull(target)) {
                return;
            }
            UiUtil.getDataset("CHANNELVARS").clear();
            ManagerConnection managerConnection = agent.getManagerConnection(agent.getPBXID());
            managerConnection.prepareChannels();
            StatusEvent activeChannel = null;
            StatusEvent customerChannel = null;
            String tegsoftTRANSFERID = Counter.getUUID().toString();
            for (int i = 0; i < managerConnection.getChannels().size(); i++) {
                StatusEvent statusEvent = managerConnection.getChannels().get(i);
                String UID = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "UID");
                if (Compare.equal(UID, UiUtil.getUID())) {
                    activeChannel = statusEvent;
                } else if (statusEvent.getChannel().startsWith(agent.getINTERFACE())) {
                    activeChannel = statusEvent;
                }
            }
            if (activeChannel == null) {
                return;
            }
            if ("extension".equals(targetType)) {
                customerChannel = managerConnection.getChannel(activeChannel.getBridgedChannel());
                managerConnection.sendAction(new RedirectAction(customerChannel.getChannel(), "tegsoft-INTERNAL", target, 1), 5000);
                return;
            } else {
                managerConnection.sendAction(new SetVarAction(activeChannel.getChannel(), "tegsoftTRANSFERID", tegsoftTRANSFERID), 5000);
                managerConnection.sendAction(new SetVarAction(activeChannel.getChannel(), "tegsoftPARTY", "AGENT"), 5000);
                managerConnection.sendAction(new SetVarAction(activeChannel.getChannel(), "tegsoftLOGID", agent.LOGID), 5000);
                customerChannel = managerConnection.getChannel(activeChannel.getBridgedChannel());
                if (customerChannel == null) {
                    return;
                }
                BaseTegsoftIVR baseTegsoftIVR = BaseTegsoftIVR.initialize(target);
                managerConnection.sendAction(new SetVarAction(customerChannel.getChannel(), "CONTID", Converter.asNotNullString(UiUtil.getDataSourceValue("TBLCRMCONTACTS", "CONTID", null, null))), 5000);
                managerConnection.sendAction(new SetVarAction(customerChannel.getChannel(), "tegsoftTRANSFERID", tegsoftTRANSFERID), 5000);
                managerConnection.sendAction(new SetVarAction(customerChannel.getChannel(), "tegsoftIVRID", target), 5000);
                managerConnection.sendAction(new SetVarAction(customerChannel.getChannel(), "tegsoftLOGID", agent.LOGID), 5000);
                if (baseTegsoftIVR.isConferenceIVR()) {
                    managerConnection.sendAction(new SetVarAction(customerChannel.getChannel(), "tegsoftPARTY", "CUSTOMER"), 5000);
                    managerConnection.sendAction(new StopMonitorAction(customerChannel.getChannel()), 5000);
                    managerConnection.sendAction(new RedirectAction(activeChannel.getChannel(), activeChannel.getBridgedChannel(), "tegsoft-INTERNAL", "9999", 1, "tegsoft-INTERNAL", "9999", 1), 5000);
                    Thread.sleep(2000);
                    managerConnection.sendAction(new RedirectAction(customerChannel.getChannel(), "tegsoft-INTERNAL", "9998", 1), 5000);
                } else {
                    managerConnection.sendAction(new RedirectAction(activeChannel.getChannel(), "tegsoft-INTERNAL", "9999", 1), 5000);
                    return;
                }
            }
        }
    }
