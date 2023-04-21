    public static String getHash(String nome) {
        if (nome == null) return null;
        try {
            MessageDigest messageDgst = MessageDigest.getInstance(Constants.DEFAULT_HASHFUNCTION);
            messageDgst.reset();
            byte[] x = messageDgst.digest(nome.getBytes());
            StringBuilder sb = new StringBuilder(x.length);
            for (int i = 0; i < x.length; i++) {
                if (x[i] > 15) sb.append(Integer.toHexString(x[i])); else if (x[i] >= 0) {
                    sb.append("0");
                    sb.append(Integer.toHexString(x[i]));
                } else sb.append(Integer.toHexString(x[i]).substring(6));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
