    protected String openTicket() throws IOException {
        URL url = new URL(URL_TICKET);
        URLConnection conn = url.openConnection();
        return getCookie(COOKIE_TICKET, conn);
    }
