    final byte[] md5Hash(String p) {
        final MD5 md5 = new MD5();
        final byte[] data = p.getBytes();
        md5.update(data, 0, data.length);
        final byte[] hash = md5.digest();
        return hash;
    }
