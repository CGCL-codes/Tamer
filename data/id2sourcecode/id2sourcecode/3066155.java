    public static String generateToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            byte id[] = session.getId().getBytes();
            byte now[] = new Long(System.currentTimeMillis()).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return (toHex(md.digest()));
        } catch (IllegalStateException e) {
            return (null);
        } catch (NoSuchAlgorithmException e) {
            return (null);
        }
    }
