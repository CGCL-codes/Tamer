    public boolean matches(TVProgramme prog) {
        String progTitle = prog.getTitle();
        if ((titleString != null) && !titleString.equals(progTitle)) {
            return false;
        }
        if ((titleContains != null) && (progTitle.indexOf(titleContains) == -1)) {
            return false;
        }
        if ((titleRegex != null) && !getTitleRegexPattern().matcher(progTitle).matches()) {
            return false;
        }
        if ((channelID != null) && !channelID.equals(prog.getChannel().getID())) {
            return false;
        }
        Time progStartTime = new Time(new Date(prog.getStart()));
        if (!afterTime.isEmpty() && afterTime.after(progStartTime)) {
            return false;
        }
        if (!beforeTime.isEmpty() && beforeTime.before(progStartTime)) {
            return false;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeZone(Application.getInstance().getTimeZone());
        cal.setTimeInMillis(prog.getStart());
        if ((dayOfWeek != -1) && (dayOfWeek != cal.get(Calendar.DAY_OF_WEEK))) {
            return false;
        }
        return true;
    }
