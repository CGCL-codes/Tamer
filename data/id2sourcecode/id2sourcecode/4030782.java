    public void moveEvent(ScrEvent event, int newTime) {
        int index = indexOf(event);
        int newIndex = getIndexAfter(newTime);
        if (newIndex == NO_SUCH_EVENT) newIndex = events_fill_p - 1; else if (event.getTime() <= newTime) newIndex -= 1;
        if (index == NO_SUCH_EVENT) {
            System.err.println("no such event error");
            for (int i = 0; i < length(); i++) {
                System.err.println("#" + i + " t " + getEventAt(i).getTime() + " p " + getEventAt(i).getPitch());
            }
            return;
        }
        if (index == EMPTY_COLLECTION) index = 0;
        event.setTime(newTime);
        Object args[] = new Object[6];
        args[0] = new Integer(index);
        args[1] = new Integer(event.getTime());
        args[2] = new Integer(event.getPitch());
        args[3] = new Integer(event.getVelocity());
        args[4] = new Integer(event.getDuration());
        args[5] = new Integer(event.getChannel());
        remoteCall(REMOTE_CHANGE, args);
        if (index < newIndex) {
            for (int i = index; i < newIndex; i++) {
                events[i] = events[i + 1];
            }
        } else {
            for (int i = index; i > newIndex; i--) {
                events[i] = events[i - 1];
            }
            events[newIndex] = event;
        }
        events[newIndex] = event;
        notifyObjectMoved(event, index, newIndex);
    }
