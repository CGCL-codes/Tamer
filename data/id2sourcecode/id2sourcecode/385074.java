    public void set_date_time(String argin) throws DevFailed {
        TangoUtil.out2.println("Entering set_date_time()");
        DateFormat df = DateFormat.getDateTimeInstance();
        System.out.println("date format = " + df.format(new Date()) + "\n" + argin);
        try {
            long d1 = df.parse(argin).getTime();
            long d2 = new Date().getTime();
            offset = d2 - d1;
        } catch (java.text.ParseException e) {
            System.out.println("date parsing error");
        }
        TangoUtil.out2.println("Exiting set_date_time()");
    }
