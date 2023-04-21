    public boolean isAvilible(String channelPath) {
        boolean avilible = true;
        try {
            String nowTime = Function.getSysTime().toString().substring(11, 16);
            SimpleDateFormat formatter1 = new SimpleDateFormat("H':'m");
            Date nowDate = formatter1.parse(nowTime);
            Date begin, end;
            int i;
            String[][] times = getChannelScheduleTime(channelPath);
            if (null != times && times.length > 0) {
                for (i = 0; i < times.length; i++) {
                    begin = null;
                    end = null;
                    if (!"".equals(times[i][0])) {
                        if (times[i][0].indexOf(":") == -1) times[i][0] = times[i][0] + ":00";
                        begin = formatter1.parse(times[i][0]);
                    }
                    if (!"".equals(times[i][1])) {
                        if (times[i][1].indexOf(":") == -1) times[i][1] = times[i][1] + ":00";
                        end = formatter1.parse(times[i][1]);
                    }
                    if ((null == begin && null == end) || (nowDate.after(begin) && null == end) || (nowDate.after(begin) && nowDate.before(end)) || (null == begin && nowDate.before(end))) {
                        avilible = false;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return avilible;
        }
    }
