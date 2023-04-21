    private static void runCommandLine(String[] args) throws ParseException {
        CronInterval c = new CronInterval(args[0]);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date arg1 = df.parse(args[1]);
        int loopCount = (args.length > 2) ? Integer.parseInt(args[2]) : 1;
        for (Iterator i = c.iterator(arg1); loopCount > 0; loopCount--) {
            Date d = (Date) i.next();
            System.out.println(loopCount + ": " + df.format(d));
        }
    }
