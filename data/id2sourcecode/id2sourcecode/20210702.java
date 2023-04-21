    public RTQueue getQueue(long id) throws RequestTrackerException {
        getSession();
        Map<String, String> attributes = Collections.emptyMap();
        final HttpGet get = new HttpGet(m_baseURL + "/REST/1.0/queue/" + id);
        try {
            final HttpResponse response = getClient().execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new RequestTrackerException("Received a non-200 response code from the server: " + responseCode);
            } else {
                if (response.getEntity() == null) {
                    LogUtils.debugf(this, "no entity returned by HTTP client");
                }
                attributes = parseResponseStream(response.getEntity().getContent());
            }
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "An exception occurred while getting queue #" + id);
            return null;
        }
        if (attributes.containsKey("id") && attributes.containsKey("name")) {
            final String queueId = attributes.get("id").replace("queue/", "");
            final long longId = Long.parseLong(queueId);
            final String name = attributes.get("name").trim();
            final String priority = attributes.get("finalpriority").trim();
            LogUtils.debugf(this, "name = %s, priority = %s", name, priority);
            if ("".equals(name) && "".equals(priority)) {
                LogUtils.debugf(this, "We got a response back, but it had no name or priority; assuming we have no access to this queue.");
                return new RTInaccessibleQueue(longId);
            }
            return new RTQueue(longId, attributes.get("name"));
        } else {
            LogUtils.debugf(this, "id or name missing (%d, %s)", attributes.get("id"), attributes.get("name"));
            return null;
        }
    }
