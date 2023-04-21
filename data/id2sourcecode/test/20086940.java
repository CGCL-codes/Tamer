    public static void setChannelVariable(ManagerConnection managerConnection, StatusEvent statusEvent, String varName, String value) throws Exception {
        if (statusEvent == null) {
            return;
        }
        if (varName == null) {
            return;
        }
        managerConnection.sendAction(new SetVarAction(statusEvent.getChannel(), varName, value), 10000);
    }
