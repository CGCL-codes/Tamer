    public Calendar getCalendar() throws IOException, ParserException {
        URL url = new URL(baseUrl + "/calendar.ics");
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(user, password.toCharArray()));
            }
        });
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        CalendarBuilder builder = new CalendarBuilder();
        return builder.build(is);
    }
