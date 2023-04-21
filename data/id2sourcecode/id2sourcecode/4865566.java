    public void testGetSchedulesByChannel() {
        try {
            ChannelMap channelMapTest = new ChannelMap();
            Calendar scheduleDay = Calendar.getInstance();
            Channel channel = new Channel("www.rai1.it", "Rai1");
            channelMapTest.add("www.rai1.it", channel);
            scheduleTest = new Schedule(channelMapTest);
            Program programTest = new Program(scheduleDay.getTime(), scheduleDay.getTime(), channel, "La Domenica Sportiva");
            scheduleDay.add(Calendar.HOUR, 1);
            Program programTest2 = new Program(scheduleDay.getTime(), scheduleDay.getTime(), channel, "Uno Mattina");
            scheduleTest.add(programTest);
            scheduleTest.add(programTest2);
            List<Program> scheduleList = new ArrayList<Program>();
            scheduleList.add(programTest);
            scheduleList.add(programTest2);
            ScheduleByChannel scheduleByChannelTest = new ScheduleByChannel(scheduleList, channel);
            assertEquals(scheduleTest.getSchedulesByChannel().get(0).getChannelName(), scheduleByChannelTest.getChannelName());
            assertEquals(scheduleTest.getSchedulesByChannel().get(0).getProgramsFromNowOn(), scheduleByChannelTest.getProgramsFromNowOn());
        } catch (ParseException e) {
            fail("Exception");
        }
    }
