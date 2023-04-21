    public static ArrayList get(byte key[]) throws Exception {
        selectAndTestHost();
        ArrayList ret = new ArrayList();
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
        buf.append("<methodName>get</methodName>");
        buf.append("<params>");
        buf.append("<param><value><base64>");
        buf.append(Base64.encodeBytes(keysha));
        buf.append("</base64></value></param>");
        buf.append("<param><value><int>10</int></value></param>");
        buf.append("<param><value><base64></base64></value></param>");
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
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String l = in.readLine();
        while (l != null && l.length() > 0) {
            l = in.readLine();
        }
        StringBuffer xml = new StringBuffer();
        while (l != null) {
            xml.append(l);
            l = in.readLine();
        }
        ByteArrayInputStream str = new ByteArrayInputStream(xml.toString().getBytes());
        XmlNode node = XmlLoader.load(str);
        XmlNode nodes[] = node.getFirstChild("params").getFirstChild("param").getFirstChild("value").getFirstChild("array").getFirstChild("data").getChild("value");
        XmlNode values[] = nodes[0].getFirstChild("array").getFirstChild("data").getChild("value");
        String placemark = nodes[1].getFirstChild("base64").getText();
        for (int i = 0; i < values.length; i++) {
            String data = values[i].getFirstChild("base64").getText();
            byte d[] = Base64.decode(data);
            ret.add(d);
        }
        return ret;
    }
