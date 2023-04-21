    public void test_setIfModifiedSinceJ() throws IOException {
        URL url = new URL("http://localhost:8080/");
        URLConnection connection = url.openConnection();
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        cal.clear();
        cal.set(2000, Calendar.MARCH, 5);
        long sinceTime = cal.getTime().getTime();
        connection.setIfModifiedSince(sinceTime);
        assertEquals("Wrong date set", sinceTime, connection.getIfModifiedSince());
    }
