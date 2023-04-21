    public int getFirstEventBefore(double time) {
        if (events_fill_p == 0) return EMPTY_COLLECTION; else if (events[0].getTime() >= time) return NO_SUCH_EVENT; else if (events[events_fill_p - 1].getTime() < time) return events_fill_p - 1;
        int min = 0;
        int max = events_fill_p - 1;
        int med = 0;
        while (max > min + 1) {
            med = (max + min) / 2;
            if (events[med].getTime() >= time) max = med; else min = med;
        }
        return min;
    }
