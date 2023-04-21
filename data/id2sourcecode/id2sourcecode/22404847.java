    public String call(Vector params) throws Exception {
        if (server == null || method == null) {
            throw new Exception("server and method cannot be null");
        }
        packet = new XmlDocument();
        packet.createRoot("methodCall");
        packet.createNewTextNode(getMethod(), "/methodCall/methodName");
        packet.addElement("params", "/methodCall");
        packet.addElement("param", "/methodCall/params");
        packet.addElement("value", "/methodCall/params/param");
        packet.addElement("struct", "/methodCall/params/param/value");
        Iterator iter = params.iterator();
        int count = 1;
        while (iter.hasNext()) {
            RpcParam param = (RpcParam) iter.next();
            packet.addElement("member", "/methodCall/params/param/value/struct");
            packet.createNewTextNode(param.getName(), "/methodCall/params/param/value/struct/member[" + count + "]/name");
            packet.addElement("value", "/methodCall/params/param/value/struct/member[" + count + "]");
            packet.createNewTextNode(param.getValue(), "/methodCall/params/param/value/struct/member[" + count + "]/value/" + param.getParamtype());
            count++;
        }
        URL url = new URL(getServer());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        System.out.println("xml = " + packet.toString());
        conn.connect();
        OutputStream os = conn.getOutputStream();
        OutputStreamWriter wout = new OutputStreamWriter(os, "UTF-8");
        BufferedReader sr = new BufferedReader(new StringReader(packet.toString()));
        String line = sr.readLine();
        while (line != null) {
            wout.write(line);
            line = sr.readLine();
        }
        wout.flush();
        os.close();
        InputStream in = conn.getInputStream();
        returnpacket = new XmlDocument(in);
        os.close();
        conn.disconnect();
        String retpackage = returnpacket.getNodeValue("/methodResponse/params/param/value/string/text()");
        if (retpackage == null) {
            retpackage = returnpacket.getNodeValue("/methodResponse/fault/value/struct/member[2]/value/string/text()");
        } else {
        }
        return retpackage;
    }
