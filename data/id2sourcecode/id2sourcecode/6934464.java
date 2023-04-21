    public static Object call(String endpoint, String method, List params) throws RemoteException, IOException {
        Map body = new HashMap<String, Object>();
        body.put("method", method);
        body.put("id", "JSONRPCClient-" + System.currentTimeMillis());
        body.put("params", params);
        String bstr = JSON.defaultJSON().forValue(body);
        byte[] buf = bstr.getBytes("UTF-8");
        URL url = new URL(endpoint);
        HttpURLConnection htc = (HttpURLConnection) url.openConnection();
        htc.setRequestMethod("POST");
        htc.setRequestProperty("Content-Length", String.valueOf(buf.length));
        htc.setDoOutput(true);
        OutputStream os = htc.getOutputStream();
        os.write(buf);
        os.flush();
        os.close();
        int clen = htc.getContentLength();
        buf = new byte[clen];
        InputStream is = htc.getInputStream();
        int p = 0;
        while (p < clen) {
            int q = is.read(buf, p, clen - p);
            if (q == -1) {
                break;
            }
            p += q;
        }
        if (p < clen) {
            throw new IOException(String.format("Invalid response: needed %d bytes, got %d", clen, p));
        }
        String rstr = new String(buf, "UTF-8");
        Map result = JSONParser.defaultJSONParser().parse(Map.class, rstr);
        if (result.get("error") != null) {
            String errMsg = String.format("Unable to invoke method %s on '%s': %s", method, endpoint, result.get("error"));
            throw new RemoteException(errMsg);
        } else if (!result.containsKey("result")) {
            throw new RemoteException("No result key in reply: " + rstr);
        }
        return result.get("result");
    }
