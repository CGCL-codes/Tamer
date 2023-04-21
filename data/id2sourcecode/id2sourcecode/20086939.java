    public static String getChannelVariable(ManagerConnection managerConnection, StatusEvent statusEvent, String varName) throws Exception {
        if (statusEvent == null) {
            return null;
        }
        if (varName == null) {
            return null;
        }
        String varValue = null;
        ManagerResponse managerResponse = managerConnection.sendAction(new GetVarAction(statusEvent.getChannel(), varName), 10000);
        if (managerResponse instanceof GetVarResponse) {
            GetVarResponse getVarResponse = (GetVarResponse) managerResponse;
            varValue = getVarResponse.getValue();
            if (NullStatus.isNull(varValue)) {
                if (NullStatus.isNotNull(statusEvent.getBridgedChannel())) {
                    managerResponse = managerConnection.sendAction(new GetVarAction(statusEvent.getBridgedChannel(), varName), 10000);
                    if (managerResponse instanceof GetVarResponse) {
                        getVarResponse = (GetVarResponse) managerResponse;
                        varValue = getVarResponse.getValue();
                    }
                }
            }
        }
        return varValue;
    }
