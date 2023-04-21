    public static InetAddress getMyGlobalIP() {
        try {
            URL url = new URL(IPSERVER);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String ip = in.readLine();
            in.close();
            con.disconnect();
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            Util.syslog(This, "getMyGobalIP()", e);
            return null;
        }
    }
