    public void addEvent(ScrEvent event) {
        int index;
        event.setDataModel(this);
        index = getIndexAfter(event.getTime());
        if (index == EMPTY_COLLECTION) index = 0; else if (index == NO_SUCH_EVENT) index = events_fill_p;
        makeRoomAt(index);
        events[index] = event;
        remoteCall(REMOTE_ADD, index, event.getTime(), event.getPitch(), event.getVelocity(), event.getDuration(), event.getChannel());
        notifyObjectAdded(event, index);
        if (isInGroup()) postEdit(new UndoableAdd(event));
    }
