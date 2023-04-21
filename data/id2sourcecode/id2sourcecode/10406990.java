    public Object clone() {
        Object o;
        try {
            o = (Matrix) super.clone();
        } catch (Exception e) {
            return null;
        }
        Matrix temp = (Matrix) o;
        temp.m_aaElement = new float[4][4];
        temp.m_aaElement[0][0] = m_aaElement[0][0];
        temp.m_aaElement[1][0] = m_aaElement[1][0];
        temp.m_aaElement[2][0] = m_aaElement[2][0];
        temp.m_aaElement[3][0] = m_aaElement[3][0];
        temp.m_aaElement[0][1] = m_aaElement[0][1];
        temp.m_aaElement[1][1] = m_aaElement[1][1];
        temp.m_aaElement[2][1] = m_aaElement[2][1];
        temp.m_aaElement[3][1] = m_aaElement[3][1];
        temp.m_aaElement[0][2] = m_aaElement[0][2];
        temp.m_aaElement[1][2] = m_aaElement[1][2];
        temp.m_aaElement[2][2] = m_aaElement[2][2];
        temp.m_aaElement[3][2] = m_aaElement[3][2];
        temp.m_aaElement[0][3] = m_aaElement[0][3];
        temp.m_aaElement[1][3] = m_aaElement[1][3];
        temp.m_aaElement[2][3] = m_aaElement[2][3];
        temp.m_aaElement[3][3] = m_aaElement[3][3];
        return o;
    }
