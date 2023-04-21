    public void add(Schedule schedule) {
        List<Transmission> transmissions = schedule.getTransmissions();
        if (!transmissions.isEmpty()) {
            Channel channel = schedule.getChannel();
            channels.add(channel);
            for (Transmission transmission : transmissions) {
                long time = transmission.getStart().getTime();
                Date pointer = new Date(time - time % module);
                IntervalEntry entry = intervals.get(pointer);
                if (entry != null) {
                    entry.add(channel, transmission);
                }
            }
        }
    }
