    public void changeEvent(ScrEvent event) {
        int index;
        index = indexOf(event);
        if (index == NO_SUCH_EVENT || index == EMPTY_COLLECTION) return;
        Object args[] = new Object[6];
        args[0] = new Integer(index);
        args[1] = new Integer(event.getTime());
        args[2] = new Integer(event.getPitch());
        args[3] = new Integer(event.getVelocity());
        args[4] = new Integer(event.getDuration());
        args[5] = new Integer(event.getChannel());
        remoteCall(REMOTE_CHANGE, args);
        notifyObjectChanged(event);
    }
