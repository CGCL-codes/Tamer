    public static Date convertToDateTZ(Date d, TimeZone fromTZ, TimeZone toTZ) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(toTZ);
        String sd = sdf.format(d);
        sdf.setTimeZone(fromTZ);
        sd = sdf.format(d);
        sdf.setTimeZone(toTZ);
        try {
            d = sdf.parse(sd);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }
