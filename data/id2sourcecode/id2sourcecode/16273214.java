    @Override
    public void add(Program p) {
        if (!this.scheduleList.isEmpty()) {
            if (this.c == p.getChannel()) {
                scheduleList.add(p);
            }
        }
    }
