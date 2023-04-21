    private String getReportTitle(long sessionPartID) {
        String spindles;
        if (spindleType == Spindle.SPINDLE_INDICATION) {
            spindles = "Spindle Indications";
        } else {
            spindles = "Visually Detected Spindles";
        }
        return db.getName(sessionPartID) + ", " + "Sampling Rate: " + db.getSamplingRate(sessionPartID) + ", " + "Channel: " + db.getChannel(sessionPartID) + "  -  " + db.getSleepDate(sessionPartID) + ", " + spindles;
    }
