    @Nonnull
    @ReturnsMutableObject(reason = "design")
    private byte[] _getDigest() {
        if (m_aDigest == null) m_aDigest = m_aMessageDigest.digest();
        return m_aDigest;
    }
