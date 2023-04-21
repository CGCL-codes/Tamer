    protected String generateNOnce(HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();
        String nOnceValue = request.getRemoteAddr() + ":" + currentTime + ":" + privateKey;
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnceValue.getBytes());
        }
        nOnceValue = md5Encoder.encode(buffer);
        return nOnceValue;
    }
