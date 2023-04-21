    final int getIndexAfter(int time) {
        if (events_fill_p == 0) return EMPTY_COLLECTION; else if (events[events_fill_p - 1].getTime() <= time) return NO_SUCH_EVENT;
        int min = 0;
        int max = events_fill_p - 1;
        while (max > min + 1) {
            int med = (max + min) / 2;
            if (events[med].getTime() <= time) min = med; else max = med;
        }
        if (time < events[min].getTime()) return min; else if (time > events[max].getTime()) return max + 1; else return max;
    }
