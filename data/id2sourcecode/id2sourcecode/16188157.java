    private void calculateCenterAndDimensions() {
        m_x = (m_minX + m_maxX) / 2;
        m_y = (m_minY + m_maxY) / 2;
        m_z = (m_minZ + m_maxZ) / 2;
        m_width = m_maxX - m_minX;
        m_height = m_maxY - m_minY;
        m_depth = m_maxZ - m_minZ;
        m_maxDimension = (m_width > m_height) ? m_width : m_height;
        if (m_depth > m_maxDimension) {
            m_maxDimension = m_depth;
        }
    }
