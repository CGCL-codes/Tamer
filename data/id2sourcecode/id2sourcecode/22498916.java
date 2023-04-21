    private void initMedianValue() {
        if (isNumValuesEven()) {
            double medianLow = m_vValues.get(m_vValues.size() / 2 - 1);
            double medianHigh = m_vValues.get(m_vValues.size() / 2);
            m_dMedianValue = (medianLow + medianHigh) / 2;
        } else {
            m_dMedianValue = m_vValues.get((m_vValues.size() - 1) / 2);
        }
    }
