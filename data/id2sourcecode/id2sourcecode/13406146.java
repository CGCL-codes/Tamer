    public void dispatch(ICSEvent evt) {
        Integer key = null;
        int type = evt.getEventType(), i = 0;
        ICSEventListener[] listeners = null;
        boolean done = false, done2 = false;
        switch(type) {
            case ICSEvent.CHANNEL_EVENT:
            case ICSEvent.TOURNAMENT_CHANNEL_EVENT:
            case ICSEvent.SHOUT_EVENT:
                key = new Integer(type * OFFSET + ((ICSChannelEvent) evt).getChannel());
                listeners = (ICSEventListener[]) chSubscribers.get(key);
                done = (listeners != null) && isChannelExclusive(type, ((ICSChannelEvent) evt).getChannel());
                break;
            case ICSEvent.BOARD_UPDATE_EVENT:
            case ICSEvent.KIBITZ_EVENT:
            case ICSEvent.WHISPER_EVENT:
            case ICSEvent.BOARD_SAY_EVENT:
            default:
                break;
        }
        if (listeners != null) for (i = 0; i < listeners.length; i++) listeners[i].icsEventDispatched(evt);
        if (!done && (listeners = subscribers[type]) != null) {
            for (i = 0; i < listeners.length; i++) listeners[i].icsEventDispatched(evt);
            done2 = true;
        }
        if (!exclusive[type] && defaultListener != null) defaultListener.icsEventDispatched(evt);
    }
