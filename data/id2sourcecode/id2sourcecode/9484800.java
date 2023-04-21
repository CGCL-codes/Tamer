    public KeyPair getUserKeys(String user) throws NotebookServerException {
        KeyPair keys = null;
        byte[] publicResult = null;
        byte[] privateResult = null;
        URL urlWithCommand = makeCommandURL("getUserKeys");
        String type = "text/xml";
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urlWithCommand.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", new Integer(0).toString());
            conn.setRequestProperty("Cookie", "$Version=0; nb_pass='");
            conn.setRequestProperty("Cookie", "$Version=0; nb_user=root");
            conn.setRequestProperty("user", user);
            conn.setRequestProperty("keytype", "pubkey");
            InputStream is = conn.getInputStream();
            int av = is.available();
            publicResult = new byte[av];
            is.read(publicResult, 0, av);
            int rc = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) urlWithCommand.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", new Integer(0).toString());
            conn.setRequestProperty("Cookie", "$Version=0; nb_pass='");
            conn.setRequestProperty("Cookie", "$Version=0; nb_user=root");
            conn.setRequestProperty("user", user);
            conn.setRequestProperty("keytype", "privkey");
            InputStream is = conn.getInputStream();
            int av = is.available();
            privateResult = new byte[av];
            is.read(privateResult, 0, av);
            int rc = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrivateKey privateKey = null;
            PublicKey publicKey = null;
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            try {
                if (privateResult != null && privateResult.length > 0) {
                    EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateResult);
                    privateKey = keyFactory.generatePrivate(privateKeySpec);
                }
            } catch (Exception e) {
                System.err.println("Error creating private key " + e);
            }
            try {
                if (publicResult != null && publicResult.length > 0) {
                    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicResult);
                    publicKey = keyFactory.generatePublic(publicKeySpec);
                }
            } catch (Exception e) {
                System.err.println("Error creating public key " + e);
            }
            keys = new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
