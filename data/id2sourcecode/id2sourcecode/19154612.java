    public synchronized void parse(String source, String target) {
        String label = source + target;
        StringBuffer url = new StringBuffer("http://finance.yahoo.com/d/quotes.csv?s=");
        url.append(label);
        url.append("=X&f=sl1d1t1ba&e=.csv");
        try {
            connection = new URL(url.toString()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String l = in.readLine();
            String[] fields = l.split(",");
            if (!"\"N/A\"".equals(fields[2])) {
                result = new BigDecimal(fields[1]);
            }
            connection = null;
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe(e.toString());
        }
    }
