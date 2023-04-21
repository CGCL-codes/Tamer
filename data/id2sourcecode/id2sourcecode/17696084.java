    protected void updateCommand(InCommand ic) {
        _state = ic.getState();
        if (isEventLogConfigured()) {
            HashMap<String, ArrayList<String>> ecmap = _sb.getEventChannelMap();
            if (ic instanceof JoinCommand) {
                JoinCommand jc = (JoinCommand) ic;
                if (ecmap.get("log").contains(jc.getChannel().toLowerCase()) && jc.getUser().equals(_sb.getIRCConnection().getClientState().getNick())) saySavedList();
            }
        }
    }
