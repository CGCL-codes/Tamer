    public byte[] sign(byte[] message) throws ServiceException {
        try {
            m_digest.update(m_sessionKey.getEncoded());
            m_digest.update(message);
            m_digest.update(m_sessionKey.getEncoded());
            byte[] t = m_digest.digest();
            return t;
        } catch (Exception ee) {
            ee.printStackTrace();
            throw new ServiceException("Cannot create signature: " + ee.getMessage());
        }
    }
