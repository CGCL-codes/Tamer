    public void scrollTo(final TVProgramme programme) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(programme.getStart());
        scrollToPadded(cal, programme.getChannel().getID());
    }
