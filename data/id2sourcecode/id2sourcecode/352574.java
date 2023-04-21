    public void moveToEnd(int loc) {
        card temp = new card();
        temp.m_type = m_data[loc].m_type;
        temp.m_color = m_data[loc].m_color;
        for (int r = loc; r < m_length - 1; ++r) {
            m_data[r] = m_data[r + 1];
        }
        m_data[m_data.length - 1].m_type = temp.m_type;
        m_data[m_data.length - 1].m_color = temp.m_color;
        m_data[m_data.length - 1].m_quantity = 0;
    }
