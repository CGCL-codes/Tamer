    private final void deleteRoomAt(int index) {
        for (int i = index; i < events_fill_p - 1; i++) events[i] = events[i + 1];
        events_fill_p--;
    }
