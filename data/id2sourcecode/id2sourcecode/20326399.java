    public boolean upload(SyncMessage syncMessage) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(createUri("upload"));
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=SM11042009");
                conn.connect();
            } catch (Exception ex) {
                AgentUtils.printMessage("Cannot connect to " + url.toString() + ": " + ex.getMessage());
                return false;
            }
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
            dos.writeBytes("--SM11042009\r\nContent-Disposition: form-data; name=\"syncmessage\"; filename=\"syncmessage\"\r\n\r\n");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(baos);
            ObjectOutputStream oos = new ObjectOutputStream(gos);
            oos.writeObject(syncMessage);
            oos.flush();
            oos.close();
            byte[] syncMessageBytes = baos.toByteArray();
            AgentUtils.printMessage("message size: " + syncMessageBytes.length);
            dos.write(syncMessageBytes);
            dos.writeBytes("\r\n--SM11042009--");
            dos.flush();
            dos.close();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
            return true;
        } catch (IOException e) {
            AgentUtils.printStackTrace(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }
