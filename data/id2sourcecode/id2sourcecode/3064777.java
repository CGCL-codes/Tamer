    private VilloNanny(String[] args) {
        formatter = new SimpleDateFormat(ConfigManager.getString("/dateFormat", "EEE dd MMMM yyyy HH:mm:ss Z"));
        String waitUntil = Util.startTimeString(args);
        if (waitUntil != null) {
            try {
                startTime = formatter.parse(waitUntil);
            } catch (ParseException e) {
                String message = "Invalid date: " + waitUntil + "; format should be like \"" + formatter.format(new Date()) + "\"";
                EventLog.log(message);
                log.error(message, e);
            }
        }
    }
