    @Override
    public void write(PrintWriter writer, StatisticsTracker stats) {
        writer.print(stats.controller.getToeThreadReport());
    }
