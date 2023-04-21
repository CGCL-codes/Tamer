    public void moveEvent(TrackEvent event, double newTime) {
        int index = indexOf(event);
        int newIndex = getIndexAfter(newTime);
        if (newIndex == NO_SUCH_EVENT) newIndex = events_fill_p - 1; else if (event.getTime() <= newTime) newIndex -= 1;
        if (index == NO_SUCH_EVENT) {
            System.err.println("no such event error");
            return;
        }
        if (index == EMPTY_COLLECTION) index = 0;
        event.setTime(newTime);
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
        notifyObjectMoved(event, index, newIndex, true);
    }
