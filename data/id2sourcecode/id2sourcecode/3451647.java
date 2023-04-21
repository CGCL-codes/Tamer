    public String getSHA1() {
        String value = null;
        if (_sha1 != null) {
            StringBuffer buffer = new StringBuffer();
            byte[] digest = _sha1.digest();
            for (int i = 0; i < digest.length; i++) {
                int un = (digest[i] >= 0) ? digest[i] : 256 + digest[i];
                buffer.append(padLeadingZeroes(Integer.toHexString(un), 2));
            }
            value = buffer.toString();
        }
        return value;
    }
