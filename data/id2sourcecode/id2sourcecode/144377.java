    public void getSchedulesWhenInc(Date start, Date end, String channel, Vector<ScheduleItem> items) {
        ScheduleItem[] schedules = (ScheduleItem[]) times.values().toArray(new ScheduleItem[0]);
        Arrays.sort(schedules);
        for (int y = 0; y < schedules.length; y++) {
            ScheduleItem item = schedules[y];
            if (item.getStop().getTime() > (start.getTime() + 5000) && item.getStart().getTime() < end.getTime()) {
                if (channel.equals(item.getChannel())) {
                    items.add(item);
                }
            }
        }
    }
