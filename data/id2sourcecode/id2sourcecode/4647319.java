    public String encryptPassword(String strOriginal) {
        if (strOriginal.isEmpty() || !m_bCanEncrypt) return (strOriginal);
        byte[] nEnc = null;
        try {
            synchronized (m_passwordEncryptor) {
                m_passwordEncryptor.reset();
                String originalSalted = strOriginal + m_strPasswordSalt;
                nEnc = m_passwordEncryptor.digest(originalSalted.getBytes());
                for (int i = 1; i < m_fnEncryptionRepeat; i++) {
                    nEnc = m_passwordEncryptor.digest(nEnc);
                }
            }
            return (new String(nEnc, "ISO-8859-1"));
        } catch (Exception e) {
            m_bCanEncrypt = false;
            return (strOriginal);
        }
    }
