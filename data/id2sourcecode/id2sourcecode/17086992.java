    public Token(HttpServletRequest req) throws ServletException {
        HttpSession session = req.getSession(true);
        long systime = System.currentTimeMillis();
        byte[] time = new Long(systime).toString().getBytes();
        byte[] id = session.getId().getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(id);
            md5.update(time);
            token = toHex(md5.digest());
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
