    public void addEvent(ScrEvent event) {
        int index;
        event.setDataModel(this);
        index = getIndexAfter(event.getTime());
        if (index == EMPTY_COLLECTION) index = 0; else if (index == NO_SUCH_EVENT) index = events_fill_p;
        makeRoomAt(index);
        events[index] = event;
        Object args[] = new Object[6];
        args[0] = new Integer(index);
        args[1] = new Integer(event.getTime());
        args[2] = new Integer(event.getPitch());
        args[3] = new Integer(event.getVelocity());
        args[4] = new Integer(event.getDuration());
        args[5] = new Integer(event.getChannel());
        remoteCall(REMOTE_ADD, args);
        notifyObjectAdded(event, index);
        if (isInGroup()) postEdit(new UndoableAdd(event));
    }
