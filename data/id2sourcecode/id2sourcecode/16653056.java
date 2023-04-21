    public final int getFirstEventAt(int time) {
        if (events_fill_p == 0) return EMPTY_COLLECTION; else if (events[events_fill_p - 1].getTime() < time) return NO_SUCH_EVENT;
        int min = 0;
        int max = events_fill_p - 1;
        int med = 0;
        while (max > min + 1) {
            med = (max + min) / 2;
            if (events[med].getTime() >= time) max = med; else min = med;
        }
        if (events[min].getTime() == time) return min; else if (events[max].getTime() == time) return max; else return NO_SUCH_EVENT;
    }
