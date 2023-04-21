    public GenericSession(U user) {
        this.user = user;
        timeLastUsed = System.currentTimeMillis();
        outgoingMessageQueue = new LinkedList<Vector<?>>();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            sessionid = new String(Base64.encodeBase64(md5.digest((user.getUserName() + timeLastUsed).getBytes())));
            System.out.println("User logged in: " + user + "(" + sessionid + ")");
        } catch (NoSuchAlgorithmException e) {
            sessionid = "";
            e.printStackTrace();
        }
    }
