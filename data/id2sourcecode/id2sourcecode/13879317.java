    public IServer lookupServer(int uin) throws GGException {
        try {
            IGGConfiguration configuration = m_session.getGGConfiguration();
            URL url = new URL(configuration.getServerLookupURL() + "?fmnumber=" + String.valueOf(uin));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(configuration.getSocketTimeoutInMiliseconds());
            con.setReadTimeout(configuration.getSocketTimeoutInMiliseconds());
            con.setDoInput(true);
            con.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), WINDOW_ENCODING));
            String line = reader.readLine();
            reader.close();
            return parseAddress(line);
        } catch (IOException ex) {
            throw new GGException("Unable to get default server for uin: " + String.valueOf(uin), ex);
        }
    }
