    private byte[] md5(final String clear) {
        if (clear == null) {
            throw new IllegalArgumentException();
        }
        try {
            byte[] data = md.digest(clear.getBytes());
            return data;
        } catch (Exception e) {
            log.log(Level.SEVERE, "md5 failed!", e);
        }
        return null;
    }
