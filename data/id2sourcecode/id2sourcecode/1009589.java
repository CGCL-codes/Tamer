    public void testParseCVS() throws Exception {
        try {
            DateFormatter.parseCVS(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            DateFormatter.parseCVS("gobbledegook");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            DateFormatter.parseCVS("ago");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            DateFormatter.parseCVS("1 junk ago");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            DateFormatter.parseCVS("1month ago");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            DateFormatter.parseCVS("last month");
            fail();
        } catch (IllegalArgumentException ex) {
        }
        Calendar now = null;
        now = Calendar.getInstance();
        now.set(Calendar.MILLISECOND, 0);
        assertEquals("parseCVS format M/dd/yy H:mm:ss z", now, DateFormatter.parseCVS(new SimpleDateFormat("M/dd/yy H:mm:ss z").format(now.getTime())), 50);
        now = Calendar.getInstance();
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.SECOND, 0);
        assertEquals("parseCVS format MMM d, yyyy h:mm a", now, DateFormatter.parseCVS(new SimpleDateFormat("MMM d, yyyy h:mm a").format(now.getTime())), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("16:30 GMT"));
        assertEquals("parseCVS format h:mm z 16:30 GMT", now, DateFormatter.parseCVS("16:30 GMT"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("16:30 EST"));
        assertEquals("parseCVS format h:mm z 16:30 EST", now, DateFormatter.parseCVS("16:30 EST"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("16:30 GMT-05:00"));
        assertEquals("parseCVS format h:mm z 16:30 GMT-05:00", now, DateFormatter.parseCVS("16:30 GMT-05:00"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("16:30 GMT+01:00"));
        assertEquals("parseCVS format h:mm z 16:30 GMT+01:00", now, DateFormatter.parseCVS("16:30 GMT+01:00"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("06:30 GMT"));
        assertEquals("parseCVS format h:mm z 06:30 GMT", now, DateFormatter.parseCVS("06:30 GMT"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("06:30 EST"));
        assertEquals("parseCVS format h:mm z 06:30 EST", now, DateFormatter.parseCVS("06:30 EST"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("06:30 GMT-05:00"));
        assertEquals("parseCVS format h:mm z 06:30 GMT-05:00", now, DateFormatter.parseCVS("06:30 GMT-05:00"), 50);
        now = Calendar.getInstance();
        now.setTime(new SimpleDateFormat("h:mm z").parse("06:30 GMT+01:00"));
        assertEquals("parseCVS format h:mm z 06:30 GMT+01:00", now, DateFormatter.parseCVS("06:30 GMT+01:00"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.WEEK_OF_MONTH, -1);
        assertEquals("parseCVS a week ago", now, DateFormatter.parseCVS("a week ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.WEEK_OF_MONTH, -1);
        assertEquals("parseCVS an week ago", now, DateFormatter.parseCVS("an week ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -14);
        assertEquals("parseCVS 1 fortnight ago", now, DateFormatter.parseCVS("1 fortnight ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -14);
        assertEquals("parseCVS 1 fortnights ago", now, DateFormatter.parseCVS("1 fortnights ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -1);
        assertEquals("parseCVS 1 minute ago", now, DateFormatter.parseCVS("1 minute ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -8);
        assertEquals("parseCVS 8 minutes ago", now, DateFormatter.parseCVS("8 minutes ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, -1);
        assertEquals("parseCVS 1 millisecond ago", now, DateFormatter.parseCVS("1 millisecond ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, -100);
        assertEquals("parseCVS 1 milliseconds ago", now, DateFormatter.parseCVS("100 milliseconds ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.SECOND, -30);
        assertEquals("parseCVS 30 second ago", now, DateFormatter.parseCVS("30 second ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.SECOND, -30);
        assertEquals("parseCVS 30 seconds ago", now, DateFormatter.parseCVS("30 seconds ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.HOUR, -2);
        assertEquals("parseCVS 2 hour ago", now, DateFormatter.parseCVS("2 hour ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.HOUR, -2);
        assertEquals("parseCVS 2 hours ago", now, DateFormatter.parseCVS("2 hours ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -2);
        assertEquals("parseCVS 2 day ago", now, DateFormatter.parseCVS("2 day ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -2);
        assertEquals("parseCVS 2 days ago", now, DateFormatter.parseCVS("2 days ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MONTH, -2);
        assertEquals("parseCVS 2 month ago", now, DateFormatter.parseCVS("2 month ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.MONTH, -2);
        assertEquals("parseCVS 2 months ago", now, DateFormatter.parseCVS("2 months ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.YEAR, -2);
        assertEquals("parseCVS 2 year ago", now, DateFormatter.parseCVS("2 year ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.YEAR, -2);
        assertEquals("parseCVS 2 years ago", now, DateFormatter.parseCVS("2 years ago"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        assertEquals("parseCVS yesterday", now, DateFormatter.parseCVS("yesterday"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        assertEquals("parseCVS tomorrow", now, DateFormatter.parseCVS("tomorrow"), 50);
        now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_WEEK) == 1) {
            now.add(Calendar.DATE, -7);
        } else {
            now.add(Calendar.DATE, 1 - now.get(Calendar.DAY_OF_WEEK));
        }
        assertEquals("parseCVS last Sunday", now, DateFormatter.parseCVS("last Sunday"), 50);
        now = Calendar.getInstance();
        now.add(Calendar.DATE, -7);
        assertEquals("parseCVS last week", now, DateFormatter.parseCVS("last week"), 50);
        now = Calendar.getInstance();
        if (now.get(Calendar.MONTH) == 0) {
            now.add(Calendar.MONTH, -12);
        } else {
            now.add(Calendar.MONTH, 0 - now.get(Calendar.MONTH));
        }
        assertEquals("parseCVS last January", now, DateFormatter.parseCVS("last January"), 50);
    }
