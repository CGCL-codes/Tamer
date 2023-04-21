    public static void setToken(HttpServletRequest request, String paramName) {
        HttpSession session = request.getSession(true);
        long systime = System.currentTimeMillis();
        byte[] time = new Long(systime).toString().getBytes();
        byte[] id = session.getId().getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance(TOKEN_DIGEST);
            md5.update(id);
            md5.update(time);
            String token = toHex(md5.digest());
            request.setAttribute(paramName, token);
            session.setAttribute(paramName, token);
        } catch (Exception e) {
        }
    }
