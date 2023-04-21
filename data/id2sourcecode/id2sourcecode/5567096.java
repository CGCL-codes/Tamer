    public void readHtmlParsing(String str) {
        HttpURLConnection con = null;
        InputStreamReader reader = null;
        try {
            URL url = new URL(str);
            con = (HttpURLConnection) url.openConnection();
            reader = new InputStreamReader(con.getInputStream(), "euc-kr");
            new ParserDelegator().parse(reader, new CallbackHandler(), true);
            con.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
