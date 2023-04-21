    public boolean symbolExists(String symbol) {
        InputStreamReader inStream;
        BufferedReader buff;
        boolean ret = true;
        try {
            URL url = new URL(provider + symbol + tags);
            URLConnection urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff = new BufferedReader(inStream);
            String csvString = buff.readLine();
            StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
            for (int i = 1; i < 3; i++) {
                if (tokenizer.hasMoreTokens()) {
                    if (tokenizer.nextToken().trim().equalsIgnoreCase("n/a")) ret = false;
                }
            }
        } catch (Exception e) {
        }
        return ret;
    }
