    public String strToSimpleTimeFormat(String timstr) throws Exception {
        java.text.SimpleDateFormat $_SimpleDateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date $_time = $_SimpleDateTimeFormat.parse(timstr);
        String $_timeStr = $_SimpleDateTimeFormat.format($_time);
        return $_timeStr;
    }
