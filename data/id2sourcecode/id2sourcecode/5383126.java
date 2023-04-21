    private HttpResponse execute(final HttpGet GET) {
        log.debug(format("executing request %s...", GET.getURI()));
        try {
            return client.execute(GET);
        } catch (final ClientProtocolException exception) {
            throw new RequestException(format("Error using protocol while requesting %s. Report as bug", GET), exception);
        } catch (final IOException exception) {
            throw new ConnectionException(format("Error connecting to service while requesting %s", GET.getURI()), exception);
        }
    }
