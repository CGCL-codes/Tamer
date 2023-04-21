    public static void main(final String[] args) {
        JTVGuide jtv = null;
        try {
            jtv = new JTVGuide();
        } catch (ParseException e) {
            PublicLogger.getLogger().error(e);
        }
        final Schedule s = jtv.schedule;
        PublicLogger.getLogger().debug("Tonight");
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 21);
        c.set(Calendar.MINUTE, 30);
        for (Program p : s.getPrograms(c.getTime())) {
            System.out.format(format, p.toString(), p.getInfo(), p.getDesc() == null ? "" : p.getDesc());
        }
        PublicLogger.getLogger().debug("Searching for *caf*");
        for (Program p : s.getProgramsByName("caf")) {
            System.out.format(format, p.toString(), p.getInfo(), p.getDesc() == null ? "" : p.getDesc());
        }
        PublicLogger.getLogger().debug("Printing sub-schedules by channel");
        final List<ScheduleByChannel> lsc = s.getSchedulesByChannel();
        for (Iterator<ScheduleByChannel> iterator = lsc.iterator(); iterator.hasNext(); ) {
            ScheduleByChannel scheduleByChannel = iterator.next();
            PublicLogger.getLogger().debug(scheduleByChannel.getChannelName());
            List<Program> lspbc = scheduleByChannel.getProgramsFromNowOn();
            for (Iterator<Program> iterator2 = lspbc.iterator(); iterator2.hasNext(); ) {
                Program p = iterator2.next();
                System.out.format(format, p.toString(), p.getInfo(), p.getDesc() == null ? "" : p.getDesc());
            }
        }
        Date d = new Date();
        c.setTime(d);
        c.add(Calendar.MINUTE, 30);
        PublicLogger.getLogger().debug("Slicing programs from " + d + " to " + c.getTime());
        for (Program p : s.getProgramsFromDateToDate(new Date(), c.getTime())) {
            System.out.format(format, p.toString(), p.getInfo(), p.getDesc() == null ? "" : p.getDesc());
        }
    }
