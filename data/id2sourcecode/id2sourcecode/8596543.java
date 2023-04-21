    private String postJSONObject(String url2, String parameterKey, String jsondata) throws RPCExceptionTunnel, CommunicationException {
        try {
            URL url = new URL(url2);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT.get());
            conn.setDoOutput(true);
            RPCUtil.encode(conn.getOutputStream(), jsondata);
            String status = conn.getHeaderField("CALL");
            String line2 = RPCUtil.decode(conn.getInputStream());
            if (status != null && status.equals("EXCEPTION")) throw new RPCExceptionTunnel(line2);
            return line2;
        } catch (IOException e) {
            throw new CommunicationException(url2, e);
        }
    }
