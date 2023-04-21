    protected String generateNOnce(MobicentsSipServletRequest request) {
        long currentTime = System.currentTimeMillis();
        String nOnceValue = request.getRemoteAddr() + ":" + currentTime + ":" + key;
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnceValue.getBytes());
        }
        nOnceValue = MD5_ECNODER.encode(buffer);
        return nOnceValue;
    }
