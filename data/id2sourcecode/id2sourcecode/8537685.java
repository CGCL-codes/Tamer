    public static byte[] requestPost(String urlString, byte[] requestData, Properties requestProperties) throws Exception {
        byte[] responseData = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            if ((requestProperties != null) && (requestProperties.size() > 0)) {
                for (Map.Entry<Object, Object> entry : requestProperties.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    String value = String.valueOf(entry.getValue());
                    con.setRequestProperty(key, value);
                }
                con.setRequestProperty("keyxx", "valuexx");
            }
            con.setRequestMethod(METHOD_POST);
            con.setDoInput(true);
            con.setDoOutput(true);
            if (requestData != null) {
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.write(requestData);
                dos.flush();
                dos.close();
            }
            int length = con.getContentLength();
            if (length != -1) {
                DataInputStream dis = new DataInputStream(con.getInputStream());
                responseData = new byte[length];
                dis.readFully(responseData);
                dis.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return responseData;
    }
