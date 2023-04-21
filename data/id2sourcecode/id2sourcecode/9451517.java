    public String getDigest() {
        Iterator t = keys.iterator();
        while (t.hasNext()) {
            String key = (String) t.next();
            digest.update(key.getBytes());
        }
        String di = gnu.beanfactory.util.Base64.encode(digest.digest());
        return di;
    }
