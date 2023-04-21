    public void moveFloor(int from, int to) {
        if (from < 0 || from >= floors.length) throw new IndexOutOfBoundsException("from");
        if (to < 0 || to >= floors.length) throw new IndexOutOfBoundsException("to");
        if (from == to) return;
        MapFloor mover = floors[from];
        if (from > to) {
            for (int i = from; i > to; i--) floors[i] = floors[i - 1];
            floors[to] = mover;
        } else {
            for (int i = from; i < to; i++) floors[i] = floors[i + 1];
            floors[to] = mover;
        }
    }
