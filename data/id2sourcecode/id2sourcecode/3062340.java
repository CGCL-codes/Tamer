    public void drop(Call call) {
        if (call instanceof SinglePartyCall) {
            SinglePartyCall spc = (SinglePartyCall) call;
            HangupAction hangupAction = new HangupAction(spc.getChannel().getDescriptor().getId());
            try {
                managerConnection.sendAction(hangupAction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (call instanceof TwoPartiesCall) {
            TwoPartiesCall tpc = (TwoPartiesCall) call;
            HangupAction hangupAction = new HangupAction(tpc.getCallerChannel().getDescriptor().getId());
            try {
                managerConnection.sendAction(hangupAction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (call instanceof ConferenceCall) {
            ConferenceCall cc = (ConferenceCall) call;
            for (Channel channel : cc.getChannels()) {
                HangupAction hangupAction = new HangupAction(channel.getDescriptor().getId());
                try {
                    managerConnection.sendAction(hangupAction);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
