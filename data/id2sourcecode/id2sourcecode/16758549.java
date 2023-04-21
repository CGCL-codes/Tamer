    public IServer[] lookupServer(final int uin) throws GGException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("lookupServer() executed for user [" + uin + "]");
        }
        try {
            final IGGConfiguration configuration = session.getGGConfiguration();
            final URL url = new URL(configuration.getServerLookupURL() + "?fmnumber=" + Integer.valueOf(uin) + "&version=8.0.0.7669");
            if (LOG.isDebugEnabled()) {
                LOG.debug("GG HUB URL address: {}", url);
            }
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(configuration.getSocketTimeoutInMiliseconds());
            con.setReadTimeout(configuration.getSocketTimeoutInMiliseconds());
            con.setDoInput(true);
            con.connect();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), WINDOWS_ENCODING));
            final String line = reader.readLine();
            reader.close();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Dane zwrócone przez serwer: {}", line);
            }
            if (line != null && line.length() > 22) {
                return parseAddress(line);
            } else {
                throw new GGException("GG HUB didn't provided a valid IP address of GG server, aborting");
            }
        } catch (final IOException ex) {
            throw new GGException("Unable to get default server for uin: " + uin, ex);
        }
    }
