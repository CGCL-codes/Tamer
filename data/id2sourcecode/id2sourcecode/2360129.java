    public static String getPreDate(String str) {
        String strRQ = str;
        java.text.SimpleDateFormat df = null;
        if (str == null) {
            return "";
        }
        if (str.length() == 10) {
            df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        } else if (str.length() == 8) {
            df = new java.text.SimpleDateFormat("yyyyMMdd");
        } else {
            return "";
        }
        try {
            java.util.Date dt = df.parse(strRQ);
            long lg = dt.getTime() - 24 * 60 * 60 * 1000;
            dt.setTime(lg);
            return df.format(dt);
        } catch (Exception e) {
            LogUtil.getLogger().debug("getPreData:\n" + e.toString());
            LogUtil.getLogger().debug(e);
            return "";
        }
    }
