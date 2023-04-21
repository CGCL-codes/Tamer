    public Matrix set(float[][] from) {
        m_aaElement[0][0] = from[0][0];
        m_aaElement[1][0] = from[1][0];
        m_aaElement[2][0] = from[2][0];
        m_aaElement[3][0] = from[3][0];
        m_aaElement[0][1] = from[0][1];
        m_aaElement[1][1] = from[1][1];
        m_aaElement[2][1] = from[2][1];
        m_aaElement[3][1] = from[3][1];
        m_aaElement[0][2] = from[0][2];
        m_aaElement[1][2] = from[1][2];
        m_aaElement[2][2] = from[2][2];
        m_aaElement[3][2] = from[3][2];
        m_aaElement[0][3] = from[0][3];
        m_aaElement[1][3] = from[1][3];
        m_aaElement[2][3] = from[2][3];
        m_aaElement[3][3] = from[3][3];
        return this;
    }
