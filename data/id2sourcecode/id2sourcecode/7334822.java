    public static void put(byte key[], byte value[]) throws Exception {
        selectAndTestHost();
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(key);
        byte[] keysha = md.digest();
        Socket s = new Socket();
        s.setSoTimeout(10000);
        s.connect(new InetSocketAddress(host, port));
        PrintStream out = new PrintStream(s.getOutputStream());
        StringBuffer buf = new StringBuffer();
        buf.append("<?xml version='1.0'?>");
        buf.append("<methodCall>");
        buf.append("<methodName>put</methodName>");
        buf.append("<params>");
        buf.append("<param><value><base64>");
        buf.append(Base64.encodeBytes(keysha));
        buf.append("</base64></value></param>");
        buf.append("<param><value><base64>");
        buf.append(Base64.encodeBytes(value));
        buf.append("</base64></value></param>");
        buf.append("<param><value><int>3600</int></value></param>");
        buf.append("<param><value><string>p2s</string></value></param>");
        buf.append("</params>");
        buf.append("</methodCall>");
        out.println("POST / HTTP/1.0");
        out.println("Host: " + host + ":" + port);
        out.println("User-Agent: p2s");
        out.println("Content-Type: text/xml");
        out.println("Content-Length: " + buf.length());
        out.println("");
        out.print(buf);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String l = in.readLine();
        while (l != null) {
            l = in.readLine();
        }
    }
