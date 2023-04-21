    public Object callService(String context, String name, Object[] args) throws Exception {
        Object replyData = null;
        String urlStr = rpcUrl;
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        configureSSL(conn);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder dataBuf = new StringBuilder();
        String reqData = MarshalUtils.marshal(new RemoteRequest(context, name, args));
        dataBuf.append(JavaRPCHandler.CMD_ARG).append("=").append(java.net.URLEncoder.encode(reqData, MarshalUtils.ENCODING));
        conn.setRequestProperty("Content-Length", String.valueOf(dataBuf.length()));
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(dataBuf.toString().getBytes());
        wr.flush();
        wr.close();
        HttpURLConnection c = (HttpURLConnection) conn;
        if (c.getResponseCode() != 200) {
            System.out.printf("Http Error From Remote Server; Code %s: %s\n", c.getResponseCode(), c.getResponseMessage());
            throw new RuntimeException(String.format("Remote API Failed with error from server; Code: %s: %s", c.getResponseCode(), c.getResponseMessage()));
        }
        System.out.printf("Http Server Response: %s: %s\n", c.getResponseCode(), c.getResponseMessage());
        BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String data = r.readLine();
        if (data == null) {
            throw new Exception("Response was null... not good.");
        }
        r.close();
        RemoteResponse resp = (RemoteResponse) MarshalUtils.unmarshal(data);
        if (resp.hasError()) {
            System.out.println("Got an Error from the remote side: " + resp.getErrorCode() + "; " + resp.getErrorMessage());
            System.out.println("========== Remote Stack Dump ===========");
            System.out.println(resp.getException());
            throw new Exception(resp.getErrorMessage());
        }
        replyData = resp.getData();
        return replyData;
    }
