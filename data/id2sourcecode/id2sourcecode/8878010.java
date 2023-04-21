    public boolean getState(Address target, String state_id, long timeout, boolean useFlushIfPresent) throws ChannelNotConnectedException, ChannelClosedException {
        String my_id = id;
        if (state_id != null) my_id += "::" + state_id;
        Address service_view_coordinator = mux.getStateProvider(target, id);
        Address tmp = getAddress();
        if (service_view_coordinator != null) target = service_view_coordinator;
        if (tmp != null && tmp.equals(target)) target = null;
        if (!mux.stateTransferListenersPresent()) return mux.getChannel().getState(target, my_id, timeout, useFlushIfPresent); else {
            View serviceView = mux.getServiceView(getId());
            boolean fetchState = serviceView != null && serviceView.size() > 1;
            return fetchState && mux.getState(target, my_id, timeout);
        }
    }
