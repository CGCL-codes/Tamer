    public void commandDisplayLogs(String args[], OutputStream output_stream) throws Exception {
        Integer tid = null;
        Date from_date = new java.util.Date((System.currentTimeMillis() - 1000 * 60 * 60));
        Date to_date = new java.util.Date();
        Vector levels = new Vector();
        if (args == null || args.length == 0) {
            args = new String[] { "-help" };
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-help")) {
                output_stream.write(("DisplayLogs\r\n" + "\t-tid              The specified thread id <default to all threads>\r\n" + "\t-from_date        The from date that will be searched from <default to the beginnig of today. The date format is yyyy_MM_dd_hh_mm>\r\n" + "\t-to_date          The to date that will be searched to <default to the current time. The date format is yyyy_MM_dd_hh_mm>\r\n" + "\t-level            The logging level <the options available is [trace][debug][critical]. You can specify all three. Defaults to critical>\r\n" + "\t-current_thread   This Displays the current working thread\r\n" + "\t-help             Displays this help\r\n").getBytes());
                return;
            }
            if (args[i].equalsIgnoreCase("-tid")) {
                tid = new Integer(args[i + 1]);
            }
            if (args[i].equalsIgnoreCase("-from_date")) {
                try {
                    from_date = simple_date_time_format.parse(args[i + 1]);
                } catch (java.text.ParseException pe) {
                    output_stream.write(("There was an error processing the from date. The correct format is [yyyy_MM_dd_hh_mm]. Your entry was [" + args[i + 1] + "]\r\n").getBytes());
                    return;
                }
            }
            if (args[i].equalsIgnoreCase("-to_date")) {
                try {
                    to_date = simple_date_time_format.parse(args[i + 1]);
                } catch (java.text.ParseException pe) {
                    output_stream.write(("There was an error processing the to date. The correct format is [yyyy_MM_dd_hh_mm]. Your entry was [" + args[i + 1] + "]\r\n").getBytes());
                    return;
                }
            }
            if (args[i].equalsIgnoreCase("-level")) {
                if (args[i + 1].equalsIgnoreCase("trace")) {
                    levels.add(Logging.TRACE);
                }
                if (args[i + 1].equalsIgnoreCase("debug")) {
                    levels.add(Logging.DEBUG);
                }
                if (args[i + 1].equalsIgnoreCase("critical")) {
                    levels.add(Logging.CRITICAL);
                }
            }
            if (args[i].equalsIgnoreCase("-current_thread")) {
                output_stream.write((ThreadId.getCurrentId() + "\r\n").getBytes());
                return;
            }
            if (levels.size() == 0) {
                levels.add(Logging.CRITICAL);
            }
        }
        Logging.Level levels_ar[] = new Logging.Level[levels.size()];
        for (int i = 0; i < levels_ar.length; i++) {
            levels_ar[i] = (Logging.Level) levels.get(i);
        }
    }
