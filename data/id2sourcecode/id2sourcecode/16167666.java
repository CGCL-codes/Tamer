    public static void set(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        long sysTime = System.currentTimeMillis();
        byte[] time = new Long(sysTime).toString().getBytes();
        byte[] id = session.getId().getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(id);
            md5.update(time);
            String token = toHex(md5.digest());
            req.setAttribute("token", token);
            session.setAttribute("token", token);
        } catch (Exception e) {
            System.err.println("Unable to calculate MD5 digests");
        }
    }
