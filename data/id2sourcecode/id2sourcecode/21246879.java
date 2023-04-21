    private ScheduleByChannel getChannelSchedule(Channel c) {
        List<Program> programList = new ArrayList<Program>();
        for (Program p : scheduleList) {
            if (p.getChannel().equals(c)) {
                programList.add(p);
            }
        }
        return new ScheduleByChannel(programList, c);
    }
