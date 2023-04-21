    public Object getObjectFromTypeAndIdentifier(String type, String id) {
        Object found = null;
        if ("node".equals(type)) {
            found = getNodes().getNode(id);
        } else if ("channel".equals(type)) {
            found = getChannels().getChannel(id);
        } else if ("reservoir".equals(type)) {
            found = getReservoirs().getReservoir(id);
        } else if ("gate".equals(type)) {
            found = getGates().getGate(id);
        } else if ("transfer".equals(type)) {
            found = getTransfers().getTransfer(id);
        }
        return found;
    }
