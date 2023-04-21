    private void displayChannelVariables() throws Exception {
        UiUtil.getDataset("CHANNELVARS").clear();
        ManagerConnection managerConnection = getManagerConnection(getPBXID());
        managerConnection.prepareChannels();
        for (int i = 0; i < managerConnection.getChannels().size(); i++) {
            StatusEvent statusEvent = managerConnection.getChannels().get(i);
            String MEMBERINTERFACE = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "MEMBERINTERFACE");
            if (NullStatus.isNull(MEMBERINTERFACE)) {
                continue;
            }
            if (Compare.equal(MEMBERINTERFACE, getINTERFACE())) {
                String CCRESULT = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "CCRESULT");
                if (NullStatus.isNotNull(CCRESULT)) {
                    DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                    dataRow.set("KEYNAME", MessageUtil.getMessage(Agent.class, Messages.CCRESULT));
                    if ("ERROR".equals(CCRESULT)) {
                        dataRow.set("KEYVALUE", MessageUtil.getMessage(Agent.class, Messages.ERROR));
                    } else if ("SUCCESS".equals(CCRESULT)) {
                        dataRow.set("KEYVALUE", MessageUtil.getMessage(Agent.class, Messages.SUCCESS));
                    }
                }
                String QUEUENAME = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "QUEUENAME");
                if (NullStatus.isNotNull(QUEUENAME)) {
                    DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                    dataRow.set("KEYNAME", MessageUtil.getMessage(Agent.class, Messages.QUEUENAME));
                    if (NullStatus.isNotNull(QUEUENAME)) {
                        Command command = new Command("SELECT NAME FROM TBLCCSKILLS WHERE UNITUID={UNITUID} AND PBXID IN (SELECT PBXID FROM TBLPBX WHERE UNITUID={UNITUID}) AND SKILL=");
                        command.bind(QUEUENAME);
                        QUEUENAME += " " + command.executeScalarAsString();
                        dataRow.set("KEYVALUE", QUEUENAME);
                    }
                }
                String QEHOLDTIME = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "QEHOLDTIME");
                if (NullStatus.isNotNull(QEHOLDTIME)) {
                    DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                    dataRow.set("KEYNAME", MessageUtil.getMessage(Agent.class, Messages.QEHOLDTIME));
                    dataRow.set("KEYVALUE", QEHOLDTIME);
                }
                String QEORIGINALPOS = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "QEORIGINALPOS");
                if (NullStatus.isNotNull(QEORIGINALPOS)) {
                    DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                    dataRow.set("KEYNAME", MessageUtil.getMessage(Agent.class, Messages.QEORIGINALPOS));
                    dataRow.set("KEYVALUE", QEORIGINALPOS);
                }
                String QUEUECALLS = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "QUEUECALLS");
                if (NullStatus.isNotNull(QUEUECALLS)) {
                    DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                    dataRow.set("KEYNAME", MessageUtil.getMessage(Agent.class, Messages.QUEUECALLS));
                    dataRow.set("KEYVALUE", QUEUECALLS);
                }
                ArrayList<String> ctiVariables = new ArrayList<String>();
                for (int k = 0; k < 100; k++) {
                    String index = "" + k;
                    if (index.length() == 1) {
                        index = "0" + index;
                    }
                    String ctiVariable = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, "tegsoftCTI" + index);
                    if (NullStatus.isNull(ctiVariable)) {
                        break;
                    }
                    ctiVariables.add(ctiVariable);
                }
                for (int k = 0; k < ctiVariables.size(); k++) {
                    String ctiVariableValue = TegsoftPBX.getChannelVariable(managerConnection, statusEvent, ctiVariables.get(k));
                    if (NullStatus.isNotNull(ctiVariableValue)) {
                        DataRow dataRow = UiUtil.getDataset("CHANNELVARS").addNewDataRow();
                        dataRow.set("KEYNAME", ctiVariables.get(k));
                        dataRow.set("KEYVALUE", ctiVariableValue);
                    }
                }
                return;
            }
        }
    }
