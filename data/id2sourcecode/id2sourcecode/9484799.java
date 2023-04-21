    public void setUserKeys(KeyPair keys, String user) throws NotebookServerException {
        URL urlWithCommand = makeCommandURL("getUserKeys");
        byte[] b = keys.getPublic().getEncoded();
        byte[] b2 = keys.getPrivate().getEncoded();
        String type = "text/xml";
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urlWithCommand.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", new Integer(b.length + b2.length).toString());
            conn.setRequestProperty("Cookie", "$Version=0; nb_pass='");
            conn.setRequestProperty("Cookie", "$Version=0; nb_user=root");
            conn.setRequestProperty("user", user);
            conn.setRequestProperty("publickeylength", new Integer(b.length).toString());
            conn.setRequestProperty("privatekeylength", new Integer(b2.length).toString());
            OutputStream os = conn.getOutputStream();
            os.write(b);
            os.write(b2);
            os.close();
            int rc = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
