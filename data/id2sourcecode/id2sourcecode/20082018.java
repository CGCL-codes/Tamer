    byte[] doHash(String str) {
        md.reset();
        md.update(str.getBytes());
        return md.digest();
    }
