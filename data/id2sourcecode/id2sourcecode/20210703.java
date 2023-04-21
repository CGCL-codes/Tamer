    private Map<String, String> getTicketAttributes(final String ticketQuery) throws RequestTrackerException {
        if (ticketQuery == null) {
            LogUtils.errorf(this, "No ticket query specified!");
            throw new RequestTrackerException("No ticket query specified!");
        }
        getSession();
        Map<String, String> ticketAttributes = Collections.emptyMap();
        final HttpGet get = new HttpGet(m_baseURL + "/REST/1.0/ticket/" + ticketQuery);
        try {
            final HttpResponse response = getClient().execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new RequestTrackerException("Received a non-200 response code from the server: " + responseCode);
            } else {
                if (response.getEntity() == null) {
                    LogUtils.debugf(this, "no entity returned by HTTP client");
                }
                ticketAttributes = parseResponseStream(response.getEntity().getContent());
            }
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "HTTP exception attempting to get ticket.");
        }
        if (ticketAttributes.size() == 0) {
            LogUtils.debugf(this, "matcher did not match %s", m_inTokensPattern.pattern());
            return null;
        }
        return ticketAttributes;
    }
