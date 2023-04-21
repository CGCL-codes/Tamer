    public String getDigest(String value) {
        if (m_md == null) {
            try {
                m_md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                nsae.printStackTrace();
            }
        }
        m_md.reset();
        byte[] input = value.getBytes();
        m_md.update(input);
        byte[] output = m_md.digest();
        m_md.reset();
        return convertToHexString(output);
    }
