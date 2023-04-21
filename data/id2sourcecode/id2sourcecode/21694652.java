    @Override
    public void processUpdate(PlayerList playerList) throws ProtocolException {
        Player p = playerList.get(getPlayerId());
        if (p == null) {
            throw new ProtocolException("Recieved PlayerPrivilegeUpdate for unknown Player with id " + getPlayerId());
        }
        boolean set;
        if (operationType == OPERATION_PERMIT) {
            set = true;
        } else if (operationType == OPERATION_REVOKE) {
            set = false;
        } else {
            throw new ProtocolException("Unknown OperationType " + operationType + " in PlayerPrivilegeUpdate");
        }
        switch(getPacketType()) {
            case 0x6A:
                if (set) {
                    p.getChannelPrivileges().add(ChannelPrivilege.ChannelAdministrator);
                } else {
                    p.getChannelPrivileges().remove(ChannelPrivilege.ChannelAdministrator);
                }
                break;
            case 0x6B:
                if (set) {
                    p.getServerPrivileges().add(ServerPrivilege.ServerAdministrator);
                } else {
                    p.getServerPrivileges().remove(ServerPrivilege.ServerAdministrator);
                }
                break;
        }
    }
