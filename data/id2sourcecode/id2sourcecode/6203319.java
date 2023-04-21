    protected net.fortuna.ical4j.model.Calendar getIcal4JCalendarFromRemoteCalendar(Calendar calendar) throws DaoException, KeyManagementException, NoSuchAlgorithmException, UnknownHostException, IOException, ParserException, IllegalBlockSizeException, BadPaddingException, DecoderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
        if (calendar == null || calendar.getUrl() == null) {
            throw new DaoException("calendar or url should not be null");
        }
        if (calendar.getUsername() != null && calendar.getPassword() != null) {
            ConnectionUtil.setAuthentication(calendar.getUsername(), calendar.getPassword());
        }
        URLConnection connection = null;
        URL url = new URL(calendar.getUrl());
        if (url.getProtocol().equalsIgnoreCase(ConnectionUtil.HTTPS_PROTOCOL)) {
            connection = ConnectionUtil.getURLConnection(url, true);
        } else {
            connection = url.openConnection();
        }
        CalendarBuilder calendarBuilder = new CalendarBuilder();
        return calendarBuilder.build(connection.getInputStream());
    }
