    public synchronized void hangupExtention() throws Exception {
        UiUtil.getDataset("CHANNELVARS").clear();
        ManagerConnection managerConnection = getManagerConnection(getPBXID());
        managerConnection.prepareChannels();
        for (int i = 0; i < managerConnection.getChannels().size(); i++) {
            StatusEvent statusEvent = managerConnection.getChannels().get(i);
            if (statusEvent.getChannel().startsWith(getINTERFACE())) {
                managerConnection.sendAction(new HangupAction(statusEvent.getChannel()), 5000);
            }
        }
    }
