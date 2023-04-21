    static byte[] makeSeed() {
        byte[] seed = null;
        StringBuffer nid = new StringBuffer(200);
        try {
            nid.append(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
        }
        nid.append(JenaRuntime.getSystemProperty("os.version"));
        nid.append(JenaRuntime.getSystemProperty("user.name"));
        nid.append(JenaRuntime.getSystemProperty("java.version"));
        nid.append(Integer.toString(Thread.activeCount()));
        nid.append(Long.toString(Runtime.getRuntime().freeMemory()));
        nid.append(Long.toString(Runtime.getRuntime().totalMemory()));
        nid.append(Long.toString(System.currentTimeMillis()));
        try {
            MessageDigest md_sha = MessageDigest.getInstance("SHA");
            seed = md_sha.digest(nid.toString().getBytes());
        } catch (NoSuchAlgorithmException ex) {
            if (!warningSent) {
                System.err.println("No SHA message digest.");
                warningSent = true;
            }
            MD5 md5 = new MD5(new ByteArrayInputStream(nid.toString().getBytes()));
            md5.processString();
            seed = md5.processString();
        }
        return seed;
    }
