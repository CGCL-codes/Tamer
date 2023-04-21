    public static String getIp(int port) throws Exception {
        String url = "http://www.myserver.org/CustomPortSniff.asp?port=" + port + "&Submit=Submit";
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        StringWriter out = new StringWriter();
        Connect.connect(conn.getInputStream(), new WriterOutputStream(out), 1024);
        String s = out.getBuffer().toString();
        if (s.indexOf("redlight.jpg") != -1) return null;
        Matcher matcher = Pattern.compile("IP address as<b>[\r\n ]*([0-9\\.]*)[\r\n ]*</b>", Pattern.DOTALL).matcher(s);
        if (matcher.find()) {
            String ip = matcher.group(1);
            return ip;
        }
        return null;
    }
