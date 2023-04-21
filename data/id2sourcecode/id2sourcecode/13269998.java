        Object execute(String method, Vector params) throws XmlRpcException, IOException {
            fault = false;
            long now = System.currentTimeMillis();
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                if (strbuf == null) strbuf = new StringBuffer();
                XmlWriter writer = new XmlWriter(strbuf);
                writeRequest(writer, method, params);
                byte[] request = writer.getBytes();
                URLConnection con = url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setAllowUserInteraction(false);
                con.setRequestProperty("Content-Length", Integer.toString(request.length));
                con.setRequestProperty("Content-Type", "text/xml");
                if (auth != null) con.setRequestProperty("Authorization", "Basic " + auth);
                OutputStream out = con.getOutputStream();
                out.write(request);
                out.flush();
                InputStream in = con.getInputStream();
                parse(in);
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
            if (debug) System.err.println("Spent " + (System.currentTimeMillis() - now) + " in request");
            return result;
        }
