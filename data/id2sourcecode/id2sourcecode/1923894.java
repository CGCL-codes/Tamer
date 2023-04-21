    private long getTomorrow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String prefix = sdf.format(new Date());
        try {
            Date d = sdf.parse(prefix);
            return d.getTime() + 86400000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() + 86400000;
    }
