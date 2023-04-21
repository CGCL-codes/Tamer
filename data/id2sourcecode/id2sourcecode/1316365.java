    public String strToSuperSimpleFormat(String timstr) throws Exception {
        java.text.SimpleDateFormat $_SimpleDateTimeFormat = new java.text.SimpleDateFormat("M-d");
        java.util.Date $_time = $_SimpleDateTimeFormat.parse(timstr);
        String $_timeStr = $_SimpleDateTimeFormat.format($_time);
        $_timeStr = $_timeStr.replaceAll("-", ".");
        return $_timeStr;
    }
