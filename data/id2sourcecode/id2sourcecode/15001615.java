    public Object execute(String method, Vector arguments) throws XmlRpcException, IOException {
        fault = false;
        long now = System.currentTimeMillis();
        try {
            StringBuffer strbuf = new StringBuffer();
            XmlWriter writer = new XmlWriter(strbuf);
            writeRequest(writer, method, arguments);
            byte[] request = strbuf.toString().getBytes();
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.setRequestProperty("Content-Length", Integer.toString(request.length));
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStream out = con.getOutputStream();
            out.write(request);
            out.flush();
            InputStream in = con.getInputStream();
            parse(in);
            System.out.println("result = " + result);
        } catch (Exception x) {
            x.printStackTrace();
            throw new IOException(x.getMessage());
        }
        if (fault) {
            XmlRpcException exception = null;
            try {
                Hashtable f = (Hashtable) result;
                String faultString = (String) f.get("faultString");
                int faultCode = Integer.parseInt(f.get("faultCode").toString());
                exception = new XmlRpcException(faultCode, faultString.trim());
            } catch (Exception x) {
                throw new XmlRpcException(0, "Invalid fault response");
            }
            throw exception;
        }
        System.out.println("Spent " + (System.currentTimeMillis() - now) + " in request");
        return result;
    }
