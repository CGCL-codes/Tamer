    static String getIP() {
        try {
            URL url = new URL("http://automation.whatismyip.com/n09230945.asp");
            final URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return rd.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
