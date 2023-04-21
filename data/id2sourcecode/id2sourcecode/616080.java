    private int overlapDepth(ScheduleItem item) {
        Calendar cal = Calendar.getInstance();
        ScheduleItem[] items = store.getScheduleArray();
        HashMap<String, Channel> channels = store.getChannels();
        Channel schChan = channels.get(item.getChannel());
        String muxString = schChan.getFrequency() + "-" + schChan.getBandWidth();
        Vector<ScheduleItem> operlapItems = new Vector<ScheduleItem>();
        for (int x = 0; x < items.length; x++) {
            if (items[x].toString().equals(item.toString()) == false) {
                if (item.isOverlapping(items[x])) {
                    operlapItems.add(items[x]);
                }
            }
        }
        cal.setTime(item.getStart());
        int duration = item.getDuration();
        int maxCount = 0;
        for (int x = 0; x < duration; x++) {
            HashMap<String, Integer> muxCountMap = new HashMap<String, Integer>();
            muxCountMap.put(muxString, new Integer(1));
            for (int y = 0; y < operlapItems.size(); y++) {
                ScheduleItem checkItem = (ScheduleItem) operlapItems.get(y);
                long slice = cal.getTime().getTime();
                if (slice > checkItem.getStart().getTime() && slice < checkItem.getStop().getTime()) {
                    Channel chackChan = channels.get(checkItem.getChannel());
                    String checkMuxString = chackChan.getFrequency() + "-" + chackChan.getBandWidth();
                    Integer muxCount = muxCountMap.get(checkMuxString);
                    if (muxCount == null) {
                        muxCountMap.put(checkMuxString, new Integer(1));
                    } else {
                        muxCountMap.put(checkMuxString, new Integer(muxCount.intValue() + 1));
                    }
                }
            }
            String[] muxTotal = muxCountMap.keySet().toArray(new String[0]);
            if (maxCount < muxTotal.length) maxCount = muxTotal.length;
            cal.add(Calendar.MINUTE, 1);
        }
        return maxCount;
    }
