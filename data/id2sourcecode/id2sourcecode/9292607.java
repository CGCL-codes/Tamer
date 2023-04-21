    public ScheduleItem[] getSchedulesWhen(Date start, Date end, String channel) {
        Vector<ScheduleItem> items = new Vector<ScheduleItem>();
        Iterator<ScheduleItem> it = times.values().iterator();
        ScheduleItem item = null;
        while (it.hasNext()) {
            item = (ScheduleItem) it.next();
            if (start.getTime() < item.getStart().getTime() && end.getTime() > item.getStart().getTime()) {
                if (channel.equals(item.getChannel())) {
                    items.add(item);
                }
            }
        }
        ScheduleItem[] itemList = (ScheduleItem[]) items.toArray(new ScheduleItem[0]);
        Arrays.sort(itemList);
        return itemList;
    }
