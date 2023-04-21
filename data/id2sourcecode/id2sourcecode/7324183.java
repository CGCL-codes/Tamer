    public DigestCredential(HttpServletRequest request, String userName, String realm, String clientDigest, String nOnce, String nC, String cnOnce, String qop, String uri) {
        this.userName = userName;
        this.realm = realm;
        this.clientDigest = clientDigest;
        try {
            if (md5Helper == null) {
                md5Helper = MessageDigest.getInstance("MD5");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest((request.getMethod() + ":" + uri).getBytes());
        }
        precomputedDigest = ":" + nOnce + ":" + nC + ":" + cnOnce + ":" + qop + ":" + md5Encoder.encode(buffer);
    }
