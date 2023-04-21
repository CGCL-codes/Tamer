    public void changeEvent(ScrEvent event) {
        int index;
        index = indexOf(event);
        if (index == NO_SUCH_EVENT || index == EMPTY_COLLECTION) return;
        remoteCall(REMOTE_CHANGE, index, event.getTime(), event.getPitch(), event.getVelocity(), event.getDuration(), event.getChannel());
        notifyObjectChanged(event);
    }
